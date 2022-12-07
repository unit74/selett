import React, { useState } from "react";
import { useRecoilState } from "recoil";
import { MemoState, TokenState, UserIdState } from "../Atom";
import { Button, useToast } from "@chakra-ui/react";
import axios from "axios";

function MemoForm({ Memo, setMemo }) {
  const [Token, setToken] = useRecoilState(TokenState);
  const [userId, setuserId] = useRecoilState(UserIdState);
  const toast = useToast();
  const updateMemo = async (id) => {
    const body = {
      id: id,
      description: Description,
    };
    setMemo(body);

    try {
      await axios.put(
        "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/profile/memos",
        body,
        {
          headers: {
            Authorization: Token,
          },
        }
      );
      toast({
        //메모 저장 성공
        position: "bottom-right",
        title: "성공",
        description: "메모가 저장되었습니다.",
        status: "success",
        duration: 2000,
        isClosable: true,
      });
    } catch (e) {
      console.log(e);
    }
  };

  const MemoHandler = (event) => {
    setDescription(event.currentTarget.value);
  };

  const [Description, setDescription] = useState(Memo ? Memo.description : "");

  return (
    Memo && (
      <div style={{ width: "100%", height: "100%" }}>
        <textarea
          value={Description || ""}
          onChange={MemoHandler}
          placeholder="메모할 내용을 입력하세요"
          style={{
            width: "90%",
            borderBottom: "1px solid #d9d9d9",
            borderTop: "1px solid #d9d9d9",
            // border: "none",
            resize: "none",
            outline: "0",
            marginTop: "15px",
            marginLeft: "20px",
            marginBottom: "10px",
            // justifyContent: "center",
            height: "87%",
            color: "black",
            fontWeight: "normal",
            overflow: "scroll",
            overflowX: "hidden",
          }}></textarea>

        <Button
          variant="outline"
          colorScheme="gray"
          onClick={() => {
            Memo && updateMemo(Memo.id);
          }}
          style={{
            marginLeft: "70%",
            height: "30px",
            width: "5%",
            flow: "right",
          }}>
          저장
        </Button>
      </div>
    )
  );
}

export default MemoForm;
