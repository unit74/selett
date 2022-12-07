import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { AddIcon } from "@chakra-ui/icons";
import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  Button,
  FormControl,
  Input,
  FormLabel,
  useDisclosure,
  useToast,
} from "@chakra-ui/react";
import QuestionList from "./QuestionList";
import {
  CoverState,
  CompanyListState,
  fileClickIdState,
  folderClickIdState,
  UserIdState,
  TokenState,
} from "../Atom";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import axios from "axios";

function Question({ Cov, setCov, Loading, setLoading }) {
  const navigate = useNavigate();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const initialRef = React.useRef(null);
  const [Content, setContent] = useState([]);
  const [Cover, setCover] = useRecoilState(CoverState);
  const [CompanyList, setCompanyList] = useRecoilState(CompanyListState);
  const [folderClickId, setfolderClickId] = useRecoilState(folderClickIdState);
  const [fileClickId, setfileClickId] = useRecoilState(fileClickIdState);
  const [placeholderProps, setPlaceholderProps] = useState({});
  const [Token, setToken] = useRecoilState(TokenState);
  const [userId, setuserId] = useRecoilState(UserIdState);
  const queryAttr = "data-rbd-drag-handle-draggable-id";
  const toast = useToast();

  const ContentHandler = (event) => {
    setContent(event.currentTarget.value);
  };

  const contentclickHandler = async () => {
    let va = false;

    if (Content) {
      Cover.map((list, index) => {
        if (list.title === Content) {
          va = true;
          return va;
        }
      });

      if (va === false) {
        // 자식에서 return 받은 title 값을 state에 저장 시킨다.
        // file 모달창에서 save 누르면 나오는 함수
        const body = {
          title: Content,
          list_id: folderClickId === 0 ? CompanyList[0].list_id : folderClickId,
          // 제일 첫 화면일 때는 0번째 리스트의 id를 반환
        };

        setLoading(true);

        try {
          await axios.post(
            "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/cover-letters",
            body,
            {
              headers: {
                Authorization: Token,
              },
            }
          );
          setLoading(false);
          await fileUpdate();
          setContent("");
        } catch (e) {
          setLoading(false);
          console.log(e);
        }
        onClose();
      } else {
        toast({
          //파일 중복 생성 우측하단 toast
          position: "bottom-right",
          title: "파일 생성 실패",
          description: "중복되는 파일이 존재합니다.",
          status: "error",
          duration: 2000,
          isClosable: true,
        });
      }
    } else {
      toast({
        //파일명이 입력되지 않았을 때 우측하단 toast
        position: "bottom-right",
        title: "파일 생성 실패",
        description: "파일명을 입력해주세요.",
        status: "error",
        duration: 2000,
        isClosable: true,
      });
    }
  };

  useEffect(() => {
    const cov = CompanyList.filter(
      (company) => company.list_id === folderClickId
    );

    if (cov[0]) {
      setCov(cov[0].title);
    }
  }, [folderClickId]);

  const fileUpdate = async () => {
    setLoading(true);

    try {
      const response = await axios.get(
        `http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=${userId}`,
        // "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=1"
        {
          headers: {
            Authorization: Token,
            // Authorization: `JWT ${Token}`,
          },
        }
      );
      setLoading(false);
      setCompanyList(response.data.list);
      const fileList = await response.data.list.filter(
        (company) =>
          company.list_id ===
          (folderClickId === 0 ? CompanyList[0].list_id : folderClickId)
        // 제일 첫 화면일 때는 0번째 리스트의 id를 반환
      );
      // 폴더의 list를 돌려서 folderClickId와 똑같은 id에 해당하는 파일의 정보를 fileList에 담는다.
      if (fileList[0]) {
        setCover(fileList[0].cover_letter);
        setCov(fileList[0].title);
      }
    } catch (e) {
      setLoading(false);
      toast({
        position: "bottom-right",
        title: "실패",
        description: "로그인 정보가 없습니다.",
        status: "error",
        duration: 2000,
        isClosable: true,
      });
      // setToken("");
      setuserId(0);
      setCover([]);
      setfileClickId(0);
      setfolderClickId(0);
      setCompanyList([]);
      // navigate("/");
    }
  };

  const onDragEnd = (result) => {
    // dropped outside the list
    // drag 끝날 때 호출되는 함수
    if (!result.destination) {
      // list밖으로 빠져나갔을 때 destination이 null로 설정됨
      // => null일 때는 그냥 리턴해줌
      return;
    }

    setCover((items) =>
      reorder(items, result.source.index, result.destination.index)
    );

    setPlaceholderProps({});
  };

  const onDragUpdate = (update) => {
    // drag 도중에 변화가 생기면 호출되는 함수
    if (!update.destination) {
      // list 밖으로 빠져나갔을 때 null로 설정
      return;
    }
    const draggableId = update.draggableId;
    const destinationIndex = update.destination.index;

    const domQuery = `[${queryAttr}='${draggableId}']`;
    const draggedDOM = document.querySelector(domQuery);
    if (!draggedDOM) {
      return;
    }
    const { clientHeight, clientWidth } = draggedDOM;

    const clientY =
      parseFloat(window.getComputedStyle(draggedDOM.parentNode).paddingTop) +
      [...draggedDOM.parentNode.children]
        .slice(0, destinationIndex)
        .reduce((total, curr) => {
          const style = curr.currentStyle || window.getComputedStyle(curr);
          const marginBottom = parseFloat(style.marginBottom);
          return total + curr.clientHeight + marginBottom;
        }, 0);

    setPlaceholderProps({
      clientHeight,
      clientWidth,
      clientY,
      clientX: parseFloat(
        window.getComputedStyle(draggedDOM.parentNode).paddingLeft
      ),
    });
  };

  const getListStyle = (isDraggingOver) => ({
    width: "100%",
    display: "flex",
    flexDirection: "column",
    // alignItems: "center",
  });

  const getItemStyle = (isDragging, draggableStyle) => ({
    color: "black",
    cursor: "pointer",
    fontWeight: "bold",
    // marginTop: "40px",

    ...draggableStyle,
  });

  const reorder = (list, startIndex, endIndex) => {
    let result = Array.from(list);

    let prev,
      next = 0;

    if (startIndex === endIndex) {
      return result;
    }

    if (endIndex === 0) {
      // 제일 첫 인덱스로 이동을 하려고 할 때,
      prev = null;
      next = result[endIndex].id;
    } else if (endIndex === result.length - 1) {
      // 제일 마지막 인덱스로 이동하려고 할 때
      prev = result[endIndex].id;
      next = null;
    } else if (endIndex - startIndex > 0) {
      prev = result[endIndex].id; // 위에서 밑으로 이동할 때
      next = result[endIndex + 1].id;
    } else {
      prev = result[endIndex - 1].id; // 밑에서 위로 이동할 때
      next = result[endIndex].id;
    }

    if (Math.abs(endIndex - startIndex) === 1 && endIndex - startIndex > 0) {
      // 한 칸 차이로 이동하려고 하면서, 위에서 밑으로 내려가려고 할 때
      if (prev !== null) {
        prev = result[endIndex].id;
      }
      if (next !== null) {
        next = result[endIndex + 1].id;
      }
    } else if (
      Math.abs(endIndex - startIndex) === 1 && // 한 칸 차이로 이동하려고 하면서, 밑에서 위로 가려고 할 때
      endIndex - startIndex < 0
    ) {
      if (prev !== null) {
        prev = result[endIndex - 1].id;
      }
      if (next !== null) {
        next = result[endIndex].id;
      }
    }

    const body = {
      id: result[startIndex].id,

      to_move_prev_id: prev,
      to_move_next_id: next,
    };
    // }
    if (result.length !== 1) {
      server(body);
    }
    return result;
  };

  const server = async (body) => {
    setLoading(true);
    try {
      await axios.put(
        "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/cover-letters/position",
        body,
        {
          headers: {
            Authorization: Token,
          },
        }
      );
      setLoading(false);
      fileUpdate();
    } catch (e) {
      setLoading(false);
      // alert("자기 자리로 이동할 수 없습니다.");
    }
  };

  const onSubmitClick = (e) => {
    if (e.key === "Enter") {
      contentclickHandler();
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
      }}>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          width: "100%",
          height: "80px",
          borderBottom: "1px solid black",
          justifyContent: "space-between",
        }}>
        <div
          style={{
            paddingLeft: "25px",
            height: "80px",
            color: "white",
            fontSize: "20px",
            width: "60%",
            fontWeight: "bold",
            lineHeight: "85px",
            overflow: "hidden",
          }}>
          {Cov}
        </div>
        <AddIcon
          onClick={onOpen}
          color="white"
          style={{
            lineHeight: "0px",
            width: "7%",
            cursor: "pointer",
            height: "80px",
            paddingTop: "2%",
            marginRight: "10%",
          }}></AddIcon>
      </div>
      <DragDropContext onDragEnd={onDragEnd} onDragUpdate={onDragUpdate}>
        <Droppable droppableId="droppable">
          {(provided, snapshot) => (
            <div
              {...provided.droppableProps}
              ref={provided.innerRef}
              style={
                // width: "100%",
                // height: "100%",
                getListStyle(snapshot.isDraggingOver)
              }>
              {Cover &&
                Cover.map((content, index) => (
                  <Draggable
                    key={`${content.id}`}
                    draggableId={`${content.id}`}
                    // 달러로 안 하면 작동 안 함
                    index={index}>
                    {(provided, snapshot) => (
                      <div
                        ref={provided.innerRef}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        style={getItemStyle(
                          snapshot.isDragging,
                          provided.draggableProps.style
                        )}>
                        <QuestionList
                          key={index}
                          content={content}
                          setCover={setCover}
                          Cover={Cover}
                          fileUpdate={fileUpdate}
                        />
                      </div>
                    )}
                  </Draggable>
                ))}
              {provided.placeholder}
            </div>
          )}
        </Droppable>
      </DragDropContext>

      <Modal initialFocusRef={initialRef} isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader style={{ marginTop: "10px" }}>
            추가할 파일의 이름을 입력해주세요
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
            <FormControl>
              <FormLabel>파일 이름</FormLabel>
              <Input
                focusBorderColor="gray.300"
                ref={initialRef}
                placeholder="file name"
                value={Content}
                onChange={ContentHandler}
                onKeyPress={onSubmitClick}
              />
            </FormControl>
          </ModalBody>

          <ModalFooter>
            <Button
              colorScheme="blue"
              mr={3}
              onClick={() => {
                // onClose();
                contentclickHandler();
              }}>
              Save
            </Button>
            <Button onClick={onClose}>Cancel</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </div>
  );
}

export default Question;
