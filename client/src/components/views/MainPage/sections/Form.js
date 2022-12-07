import React, { Fragment, useState, useEffect, useRef } from "react";
import { useRecoilState } from "recoil";
import { useNavigate } from "react-router-dom";
import { Button, Tag } from "@chakra-ui/react";
import Count from "./Count";
import { LockIcon, UnlockIcon } from "@chakra-ui/icons";
import axios from "axios";
import {
  TokenState,
  UserIdState,
  CoverState,
  fileClickIdState,
  folderClickIdState,
  CompanyListState,
} from "../Atom";

function Form() {
  // 맞춤법 검사가 꺼져있음
  const navigate = useNavigate();
  const [Title, setTitle] = useState("");
  let [Text, setText] = useState("");
  const [Grammer, setGrammer] = useState(false);
  const [questionLock, setquestionLock] = useState(false);
  const [descriptionLock, setdescriptionLock] = useState(false);
  const [Cover, setCover] = useRecoilState(CoverState);
  const [fileClickId, setfileClickId] = useRecoilState(fileClickIdState);
  const [folderClickId, setfolderClickId] = useRecoilState(folderClickIdState);
  const [CompanyList, setCompanyList] = useRecoilState(CompanyListState);
  const [change, setchange] = useState(false);
  const [Token, setToken] = useRecoilState(TokenState);
  const [userId, setuserId] = useRecoilState(UserIdState);
  const [second, setsecond] = useState(0);
  const [typing, setTyping] = useState(false);
  const [AutoSave, setAutoSave] = useState(false);

  const countInterval = () => {
    if (second === 0) {
      // titlebuttonHandler();
      // textbuttonHandler();
      AutoHandler();

      clearInterval(count);
    } else {
      setsecond(() => second - 1);
    }
  };

  let count;

  useEffect(() => {
    if (!CompanyList || CompanyList.length === 0) {
      return;
    }
    count = setInterval(countInterval, 1000);
    return () => clearInterval(count);
  }, [second, typing]);

  useEffect(() => {
    // 제일 첫 화면에서 회사 목록이 불러진 후 실행
    if (!CompanyList || CompanyList.length === 0) {
      return;
    }
    if (
      CompanyList[0] &&
      fileClickId === 0 &&
      folderClickId === 0 &&
      change === false
    ) {
      // 회사 목록이 존재하면서 파일 클릭이 되어있지 않고 폴더 클릭이 되어 있지 않을 때
      if (fileClickId === 0) {
        setchange(true);
        setTitle(CompanyList[0].cover_letter[0].question);
        setText(CompanyList[0].cover_letter[0].description);
        setdescriptionLock(CompanyList[0].cover_letter[0].description_lock);
        setquestionLock(CompanyList[0].cover_letter[0].question_lock);
      }
    }
  });

  useEffect(() => {
    if (!Cover || Cover.length === 0) {
      return;
    }
    if (Cover[0] && folderClickId) {
      // 파일 목록이 불러와졌고, 폴더 클릭이 있으면

      if (Cover[0].question === null || Cover[0].question === undefined) {
        // null일 땐 빈 칸
        setTitle("");
      } else {
        // null 아니면
        setTitle(Cover[0].question); // question 입력
      }

      if (Cover[0].description === null || Cover[0].description === undefined) {
        setText("");
      } else {
        setText(Cover[0].description);
      }
    }
  }, [folderClickId]); // 폴더 클릭할 때마다 바뀜

  useEffect(() => {
    if (!Cover || Cover.length === 0) {
      return;
    }
    if (Cover[0] && fileClickId) {
      // Cover가 하나라도 존재하는 상태에서

      const cov = Cover.filter((cover) => cover.id === fileClickId);
      // Question 파일에서 클릭한 아이디와 현재 cover에 있는 id가 동일한 것을 cov에 담음

      if (cov[0].question === null || cov[0].question === undefined) {
        // cov에 담겨있는 것 중 질문이 null이거나 undefined면 Title을 공백으로 설정
        setTitle("");
      } else {
        setTitle(cov[0].question);
        // 아니라면, 질문에 있는 값을 출력
      }

      if (cov[0].description === null || cov[0].description === undefined) {
        // cov에 담겨있는 것중 description이 null이거나 undefined면 공백으로 설정
        setText("");
      } else {
        setText(cov[0].description);
        // 아니라면, description에 있는 값을 출력
      }

      setquestionLock(cov[0].question_lock); // 잠금 유무 정보도 받아옴
      setdescriptionLock(cov[0].description_lock);
    }
  }, [fileClickId]); // 클릭한 파일의 아이디가 바뀔 때마다 실행

  const titleHandler = (event) => {
    clearInterval(count);
    setTyping(!typing);
    setTitle(event.currentTarget.value);
    if (Title !== 0 && Title !== null && Title !== undefined) {
      if (Title.length > 200) {
        alert("200자 이상입니다.");
        setTitle(Title.substring(0, 200));
      }
    }

    setsecond(3);
  };

  const textHandler = (event) => {
    clearInterval(count);
    setText(event.currentTarget.value);
    setTyping(!typing);

    // console.log(event.currentTarget.value.length);
    if (Text !== 0 && Text !== null && Text !== undefined) {
      if (Text.length > 5000) {
        alert("5000자 이상입니다.");
        setText(Text.substring(0, 5000));
      }
    }
    setsecond(3);
  };

  const AutoHandler = async () => {
    if (!Cover || Cover.length === 0) {
      return;
    }

    setAutoSave(true);
    const body = {
      id: fileClickId === 0 ? Cover[0].id : fileClickId,
      question: Title,
      description: Text,
    };

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
      await FormUpdate();
      setAutoSave(false);
    } catch (e) {
      console.log(e);
    }
  };

  const titlebuttonHandler = async () => {
    // if (questionLock === false) {
    if (!Cover || Cover.length === 0) {
      return;
    }
    let body = {};

    if (questionLock === false) {
      body = {
        // id: fileClickId === 0 ? CompanyList[0].cover_letter[0].id : fileClickId,
        id: fileClickId === 0 ? Cover[0].id : fileClickId,
        // CompanyList[0]이니까 0번 폴더에 저장이 되지.
        description: Text,
        question: Title,
        question_lock: !questionLock,
      };
    } else if (questionLock === true) {
      body = {
        id: fileClickId === 0 ? Cover[0].id : fileClickId,
        description: Text,
        question_lock: !questionLock,
      };
    }
    // setTitle(Title);
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
      await FormUpdate();
    } catch (e) {
      console.log(e);
    }
    // }
    setquestionLock(!questionLock);
    // console.log("실행");
    // 질문 자물쇠
  };

  const textbuttonHandler = async () => {
    if (!Cover || Cover.length === 0) {
      return;
    }
    // setText(Text);
    let body = {};

    if (descriptionLock === false) {
      body = {
        id: fileClickId === 0 ? Cover[0].id : fileClickId,
        question: Title,
        description: Text,
        description_lock: !descriptionLock,
      };
    } else if (descriptionLock === true) {
      body = {
        question: Title,
        id: fileClickId === 0 ? Cover[0].id : fileClickId,
        description_lock: !descriptionLock,
      };
    }

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
      await FormUpdate();
    } catch (e) {
      console.log(e);
    }

    setdescriptionLock(!descriptionLock);
    // 내용 자물쇠
  };

  const FormUpdate = async () => {
    try {
      const response = await axios.get(
        `http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/?userId=${userId}`,
        {
          headers: {
            Authorization: Token,
            // Authorization: `JWT ${Token}`,
          },
        }
      );
      setCompanyList(response.data.list);
      const fileList = await response.data.list.filter(
        (company) =>
          company.list_id ===
          (folderClickId === 0 ? CompanyList[0].list_id : folderClickId)
        // 제일 첫 화면일 때는 0번째 리스트의 id를 반환
      );

      if (fileList[0]) {
        const co = fileList[0].cover_letter;
        setCover(fileList[0].cover_letter);

        const FormList = co.filter(
          (form) =>
            form.id ===
            (fileClickId === 0
              ? CompanyList[0].cover_letter[0].id
              : fileClickId)
        );

        if (FormList[0]) {
          setTitle(FormList[0].question);
          setText(FormList[0].description);
          setquestionLock(FormList[0].question_lock);
          setdescriptionLock(FormList[0].description_lock);
        }
      }
    } catch (e) {
      // navigate("/");
      // setToken("");
      setuserId(0);
      setCover([]);
      setfileClickId(0);
      setfolderClickId(0);
      setCompanyList([]);
    }
  };

  const calc = (text, blank = 0) => {
    let word = 0;

    if (blank === 0 && text !== null && text !== undefined) {
      text = text.replace(/\s+/g, "");
    }

    if (text !== 0 && text !== null && text !== undefined) {
      word = text.length;
    }
    return word;
  };

  const byteCounter = (text, blank = 0) => {
    let byte = 0;

    if (blank === 0 && text !== null && text !== undefined) {
      text = text.replace(/\s+/g, "");
    }

    if (text !== 0 && text !== null && text !== undefined) {
      for (let i = 0; i < text.length; i++) {
        if (/[ㄱ-ㅎㅏ-ㅣ가-힣一-龥ぁ-ゔァ-ヴー々〆〤]/.test(text[i])) {
          byte = byte + 2;
        } else {
          byte++;
        }
      }
    }

    return byte;
  };

  return (
    <Fragment>
      <div
        style={{
          // 흰색 블록
          backgroundColor: "white",
          marginTop: "30px",
          marginLeft: "25px",
          width: { Grammer } ? "93%" : "70%",
          height: "90%",
          overflow: "hidden",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          color: "black",
        }}>
        {AutoSave ? (
          <div
            style={{
              height: "30px",
              marginTop: "5px",
              color: "gray",
              justifyContent: "right",
              marginLeft: "86%",
              paddingTop: "2px",
            }}>
            저장 중 ...
          </div>
        ) : (
          <div style={{ height: "30px", marginTop: "5px" }}></div>
        )}
        <div // 제목
          style={{
            width: "93%",
            height: "7%",
            border: "solid 2px #d9d9d9",
            marginTop: "5px",
            borderRadius: "5px",
            display: "flex",
          }}>
          <input // 제목 input
            type="text"
            style={{
              marginLeft: "10px",
              width: "95%",
              height: "99%",
              border: "none",
              outline: "0",
            }}
            value={Title || ""}
            onChange={titleHandler}
            placeholder="제목을 입력해주세요"
            readOnly={questionLock ? true : false}></input>
          <Button // 제목 버튼
            colorScheme="gray"
            variant="ghost"
            style={{
              width: "40px",
              height: "90%",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              marginTop: "1px",
              marginLeft: "10px",
              border: "none",
              outline: "0",
            }}
            onClick={titlebuttonHandler}>
            {questionLock ? <LockIcon /> : <UnlockIcon />}

            {/* 제목 버튼 안에 자물쇠 아이콘 */}
          </Button>
        </div>
        <div // 내용 시작
          style={{
            borderRadius: "5px",
            width: "93%",
            height: "80%",
            border: "solid 2px #d9d9d9",
            marginTop: "20px",
            display: "flex",
            flexDirection: "column",
          }}>
          <div style={{ display: "flex", width: "100%", height: "100%" }}>
            <textarea // 내용 입력 칸
              type="text"
              style={{
                width: "95%",
                height: "97%",
                border: "none",
                marginLeft: "10px",
                marginTop: "10px",
                resize: "none",
                outline: "0",
              }}
              placeholder="내용을 입력해주세요"
              readOnly={descriptionLock ? true : false}
              value={Text || ""}
              onChange={textHandler}></textarea>
            <Button // 내용 버튼
              colorScheme="gray"
              variant="ghost"
              style={{
                width: "40px",
                height: "40px",
                marginTop: "1px",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                marginLeft: "10px",
                border: "none",
                outline: "0",
              }}
              onClick={textbuttonHandler}>
              {descriptionLock ? <LockIcon /> : <UnlockIcon />}
              {/* 내용 버튼안의 자물쇠 아이콘 */}
            </Button>
          </div>
          <Count calc={calc} Text={Text} byteCounter={byteCounter} />
          {/* 글자수를 세주는 폼 */}
        </div>
      </div>
    </Fragment>
  );
}

export default Form;
