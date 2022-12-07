import React, { Fragment, useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { AddIcon, DeleteIcon } from "@chakra-ui/icons";
import { useNavigate } from "react-router-dom";
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
  Avatar,
  AvatarBadge,
  useToast,
} from "@chakra-ui/react";
import axios from "axios";
import {
  folderClickIdState,
  CoverState,
  CompanyListState,
  TokenState,
  UserIdState,
  fileClickIdState,
} from "../Atom";
import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import { Navigate } from "react-router-dom";

function Folder({ Loading, setLoading }) {
  const navigate = useNavigate();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const initialRef = React.useRef(null);
  const [Company, setCompany] = useState("");
  const [folderClickId, setfolderClickId] = useRecoilState(folderClickIdState);
  const [fileClickId, setfileClickId] = useRecoilState(fileClickIdState);
  const [Cover, setCover] = useRecoilState(CoverState);
  const [CompanyList, setCompanyList] = useRecoilState(CompanyListState);
  // const [Loading, setLoading] = useState(false);
  const [placeholderProps, setPlaceholderProps] = useState({});
  const queryAttr = "data-rbd-drag-handle-draggable-id";
  const [Prev, setPrev] = useState("");
  const [Next, setNext] = useState("");
  const [Token, setToken] = useRecoilState(TokenState);
  const [userId, setuserId] = useRecoilState(UserIdState);
  const toast = useToast();

  const companyHandler = (event) => {
    // 회사의 이름 적는 칸 실시간으로 받아와서 Company에 저장
    setCompany(event.currentTarget.value);
  };

  const companyclickHandler = async () => {
    // save 버튼을 눌렀을 때 작동하는 코드

    let va = false; // 일단 false로 저장해놓음(중복 여부 / 중복일 시 true)

    if (Company && Company.length > 0) {
      // 만약에 폴더 이름을 적는 칸에 문자가 있으면
      CompanyList.map((list, index) => {
        // map 시켜서
        if (list.title === Company) {
          // 이전에 있던 것들과 중복인지 검사
          va = true; // 중복이라면 true로 바꿔줌
          return va;
        }
      });
      if (va === false) {
        // 중복이 아니라면
        // 자식에서 return 받은 company 값을 state에 저장시켜준다.
        // 폴더의 모달창에서 save 버튼을 누르면 입력한 이름이 company로 반환되고 여기로 들어옴

        const body = {
          user_id: userId,
          title: Company,
        };

        setLoading(true);

        try {
          await axios.post(
            "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/lists",
            body,
            {
              headers: {
                Authorization: Token,
              },
            }
          );
          // 서버에게 요청하고,
          setLoading(false);
          setCompany(""); // 회사가 적혀있는 칸은 다시 공백으로 만듦

          await FolUpdate1(); // 요청한 다음에는 FolUpdate 함수 써줌
        } catch (e) {
          setLoading(false);
          toast({
            position: "bottom-right",
            title: "폴더 생성 실패",
            status: "error",
            duration: 2000,
            isClosable: true,
          });
        }
        onClose(); // 모달창을 닫아주는 코드
      } else {
        toast({
          //폴더 중복 생성시 우측하단 toast
          position: "bottom-right",
          title: "폴더 생성 실패",
          description: "중복되는 폴더가 존재합니다.",
          status: "error",
          duration: 2000,
          isClosable: true,
        });
      }
    } else {
      toast({
        //폴더명이 입력되지 않았을 때 우측하단 toast
        position: "bottom-right",
        title: "폴더 생성 실패",
        description: "폴더명을 입력해주세요.",
        status: "error",
        duration: 2000,
        isClosable: true,
      });
    }
  };

  const deleteHandler = async (id) => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      // 삭제 버튼을 누르면 확인창 띄움
      // setCompanyList(CompanyList.filter((company) => company.list_id !== id));
      // 확인을 눌렀다면, 누른 id와 리스트의 id를 비교해서 다른 것들로만 추출하여 CompanyList에 다시 담음
      const body = {
        listId: id,
      };
      setLoading(true);

      try {
        await axios.delete(
          "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/lists",
          {
            params: body,
            headers: {
              Authorization: Token,
            },
          }
        );
        setLoading(false);

        await FolUpdate();
      } catch (e) {
        setLoading(false);

        toast({
          position: "bottom-right",
          title: "폴더 삭제 실패",
          description: e.response.data,
          status: "error",
          duration: 2000,
          isClosable: true,
        });
      }
    }
  };

  const FolUpdate = async () => {
    // 서버에서 새로 값들을 받아옴(폴더에 관한 내용 처리)
    setLoading(true);
    try {
      const response = await axios.get(
        // "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=1"
        `http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=${userId}`,
        {
          headers: {
            Authorization: Token,
            // Authorization: `JWT ${Token}`,
          },
        }
      );
      setCompanyList(response.data.list);
      circleClick(response.data.list[0].list_id);
      setLoading(false);
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

  const FolUpdate1 = async () => {
    // 서버에서 새로 값들을 받아옴(폴더에 관한 내용 처리)
    setLoading(true);
    try {
      const response = await axios.get(
        // "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=1"
        `http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=${userId}`,
        {
          headers: {
            Authorization: Token,
            // Authorization: `JWT ${Token}`,
          },
        }
      );
      setCompanyList(response.data.list);

      setLoading(false);
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

  const circleClick = (id) => {
    const cov = CompanyList.filter((company) => company.list_id === id);
    // CompanyList에 담겨져있는 폴더들을 살피면서 클릭한 id와 같은 것을 추출해냄
    setCover(cov[0].cover_letter);
    setfolderClickId(id);
    // 폴더를 클릭했을 때 id를 mainPage로 보내줌
    setfileClickId(cov[0].cover_letter[0].id);
  };

  const onDragEnd = (result) => {
    // dropped outside the list
    // drag 끝날 때 호출되는 함수
    if (!result.destination) {
      // list밖으로 빠져나갔을 때 destination이 null로 설정됨
      // => null일 때는 그냥 리턴해줌
      return;
    }

    setCompanyList((items) =>
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
    alignItems: "center",
  });

  const getItemStyle = (isDragging, draggableStyle) => ({
    color: "#212226",
    cursor: "pointer",
    fontWeight: "bold",
    marginTop: "40px",

    ...draggableStyle,
  });

  const reorder = (list, startIndex, endIndex) => {
    let result = Array.from(list);

    let prev,
      next = 0;

    if (endIndex === startIndex) {
      return result;
    }

    if (endIndex === 0) {
      // 제일 첫 인덱스로 이동을 하려고 할 때,
      prev = null;
      next = result[endIndex].list_id;
    } else if (endIndex === result.length - 1) {
      // 제일 마지막 인덱스로 이동하려고 할 때
      prev = result[endIndex].list_id;
      next = null;
    } else if (endIndex - startIndex > 0) {
      prev = result[endIndex].list_id; // 위에서 밑으로 이동할 때
      next = result[endIndex + 1].list_id;
    } else {
      prev = result[endIndex - 1].list_id; // 밑에서 위로 이동할 때
      next = result[endIndex].list_id;
    }

    if (Math.abs(endIndex - startIndex) === 1 && endIndex - startIndex > 0) {
      // 한 칸 차이로 이동하려고 하면서, 위에서 밑으로 내려가려고 할 때
      if (prev !== null) {
        prev = result[endIndex].list_id;
      }
      if (next !== null) {
        next = result[endIndex + 1].list_id;
      }
    } else if (
      Math.abs(endIndex - startIndex) === 1 && // 한 칸 차이로 이동하려고 하면서, 밑에서 위로 가려고 할 때
      endIndex - startIndex < 0
    ) {
      if (prev !== null) {
        prev = result[endIndex - 1].list_id;
      }
      if (next !== null) {
        next = result[endIndex].list_id;
      }
    }

    const body = {
      list_id: result[startIndex].list_id,

      to_move_prev_list_id: prev,
      to_move_next_list_id: next,
    };

    if (result.length !== 1) {
      server(body);
    }

    // const [removed] = result.splice(startIndex, 1);
    // result.splice(endIndex, 0, removed);

    return result;
  };

  const server = async (body) => {
    setLoading(true);

    try {
      await axios.put(
        "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/lists/position",
        body,
        {
          headers: {
            Authorization: Token,
          },
        }
      );
      setLoading(false);

      await FolUpdate();
    } catch (e) {
      setLoading(false);
      console.log(e);
    }
  };

  const onSubmitClick = (e) => {
    if (e.key === "Enter") {
      companyclickHandler();
    }
  };

  return (
    <Fragment>
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
              {CompanyList &&
                CompanyList.map((company, index) => (
                  <Draggable
                    key={`${company.list_id}`}
                    draggableId={`${company.list_id}`}
                    // 달러로 안 하면 작동 안 함
                    index={index}>
                    {/* index는 변하는 값, draggableId는 안 변함 */}
                    {(provided, snapshot) => (
                      <Avatar // 폴더의 동그라미
                        ref={provided.innerRef}
                        {...provided.draggableProps}
                        {...provided.dragHandleProps}
                        type="button"
                        className="rounded-circle"
                        onClick={() => circleClick(company.list_id)}
                        name={company.title}
                        getInitials={() => `${company.title}`}
                        key={index}
                        size="50"
                        width="55px"
                        height="55px"
                        _hover={{ width: "60px", height: "60px" }}
                        bg="white"
                        style={getItemStyle(
                          snapshot.isDragging,
                          provided.draggableProps.style
                        )}>
                        <AvatarBadge
                          borderColor="white"
                          bg="white"
                          boxSize="1.25em">
                          <DeleteIcon
                            style={{ width: "90%", height: "90%" }}
                            onClick={() => deleteHandler(company.list_id)}
                          />
                        </AvatarBadge>
                      </Avatar>
                    )}
                  </Draggable>
                ))}
              {provided.placeholder}
            </div>
          )}
        </Droppable>
      </DragDropContext>

      <button // 폴더의 동그라미
        className="rounded-circle"
        onClick={onOpen}
        style={{
          minWidth: "55px",
          minHeight: "55px",
          backgroundColor: "white",
          marginTop: "40px",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}>
        <AddIcon />
      </button>
      <Modal initialFocusRef={initialRef} isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader style={{ marginTop: "10px" }}>
            이름을 정해주세요
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody pb={6}>
            <FormControl>
              <FormLabel>회사 이름</FormLabel>
              <Input
                ref={initialRef}
                placeholder="Company"
                value={Company}
                onChange={companyHandler}
                focusBorderColor="gray.300"
                onKeyPress={onSubmitClick}
              />
            </FormControl>
          </ModalBody>

          <ModalFooter>
            <Button
              colorScheme="blue"
              mr={3}
              onClick={() => {
                companyclickHandler();
                // onClose();
              }}>
              Save
            </Button>
            <Button
              onClick={() => {
                onClose();
                setCompany("");
              }}>
              Cancel
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Fragment>
  );
}

export default Folder;
