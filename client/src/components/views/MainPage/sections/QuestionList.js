import React, { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { DeleteIcon, EditIcon, CheckIcon } from "@chakra-ui/icons";
import { Box, useToast } from "@chakra-ui/react";
import axios from "axios";
import {
  fileClickIdState,
  CoverState,
  folderClickIdState,
  TokenState,
  UserIdState,
} from "../Atom";

function QuestionList({ content, fileUpdate }) {
  const [Edit, setEdit] = useState(false);
  const [Text, setText] = useState(content.title);
  const [fileClickId, setfileClickId] = useRecoilState(fileClickIdState);
  const [Cover, setCover] = useRecoilState(CoverState);
  const [folderClickId, setfolderClickId] = useRecoilState(folderClickIdState);
  const [Token, setToken] = useRecoilState(TokenState);
  const toast = useToast();
  const [userId, setuserId] = useRecoilState(UserIdState);

  useEffect(() => {
    setEdit(false);
  }, [folderClickId]);

  const deleteClick = async (id) => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      // setCover(Cover.filter((content) => content.id !== id));
      const body = {
        id: id,
      };

      try {
        await axios.delete(
          "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/cover-letters",
          {
            params: body,
            headers: {
              Authorization: Token,
            },
          }
        );
        await fileUpdate();
      } catch (e) {
        console.log(e);
      }
    }
  };

  const onChangeHandler = (event) => {
    setText(event.currentTarget.value);
  };

  const EditClick = (id) => {
    setEdit(true);
    const EditTitle = Cover.filter((cover) => cover.id === id);
    if (EditTitle[0]) {
      setText(EditTitle[0].title);
    }
  };

  const CheckOnClick = async (id) => {
    const body = {
      id: id,
      title: Text,
    };
    // const editCover = Cover.map((cover) => ({
    //   ...cover,
    //   title: cover.id === id ? Text : cover.title,
    // }));
    // setCover(editCover);
    try {
      await axios.put(
        "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/cover-letters",
        body,
        {
          headers: {
            Authorization: Token,
          },
        }
      );
      await fileUpdate();
    } catch (e) {
      toast({
        //회원가입 중복 우측하단 toast
        position: "bottom-right",
        title: "파일 이름 수정 오류",
        description: e.response.data,
        status: "error",
        duration: 2000,
        isClosable: true,
      });
    }

    setEdit(false);
  };

  const questionClick = (id) => {
    setfileClickId(id);
  };

  return (
    <Box style={{ width: "100%" }} _hover={{ backgroundColor: "gray.800" }}>
      {Edit ? (
        <div style={{ display: "flex", height: "50px" }}>
          <input
            value={Text}
            onChange={onChangeHandler}
            style={{
              paddingLeft: "25px",
              height: "50px",
              color: "white",
              backgroundColor: "#303136",
              fontSize: "15px",
              width: "90%",
              marginTop: "10px",
              overflow: "scroll",
              // float: "left",
              border: "none",
              outline: "0",
            }}></input>
          <CheckIcon
            style={{
              color: "white",
              height: "17px",
              width: "10%",
              marginTop: "10%",
              marginRight: "4%",
              cursor: "pointer",
            }}
            onClick={() => CheckOnClick(content.id)}
          />
        </div>
      ) : (
        <div style={{ display: "flex", width: "100%" }}>
          <Box
            style={{
              paddingLeft: "25px",
              height: "40px",
              color: "white",
              fontSize: "15px",
              width: "80%",
              marginTop: "9%",
              overflow: "scroll",
              border: "none",
              outline: "0",
            }}
            onClick={() => questionClick(content.id)}
            sx={{ "::-webkit-scrollbar": { display: "none" } }}>
            {content.title || ""}
          </Box>
          <EditIcon
            style={{
              color: "white",
              height: "17px",
              width: "10%",
              marginTop: "9%",
              marginRight: "4%",
              cursor: "pointer",
            }}
            onClick={() => EditClick(content.id)}
          />
          <DeleteIcon
            style={{
              color: "white",
              height: "17px",
              width: "10%",
              marginTop: "9%",
              marginRight: "4%",
              cursor: "pointer",
            }}
            onClick={() => deleteClick(content.id)}
          />
        </div>
      )}
    </Box>
  );
}

export default QuestionList;
