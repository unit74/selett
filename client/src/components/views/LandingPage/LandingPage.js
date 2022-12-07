import React, { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import "./LandingPage.css";
import { useNavigate } from "react-router-dom";
import Carousel from "react-bootstrap/Carousel";
import sellet from "../../../imageFolder/1.png";
import sellet2 from "../../../imageFolder/2.png";
import {
  InputGroup,
  Input,
  InputRightElement,
  Button,
  useToast,
} from "@chakra-ui/react";
import { TokenState, UserIdState } from "../MainPage/Atom";
import axios from "axios";

function LandingPage() {
  const navigate = useNavigate(); // navigate 선언
  const [Show, setShow] = useState(false);
  const [Id, setId] = useState("");
  const [Password, setPassword] = useState("");
  const handleClick = () => setShow(!Show);
  const toast = useToast();
  const [Token, setToken] = useRecoilState(TokenState);
  const [UserId, setUserId] = useRecoilState(UserIdState);

  const IdHandler = (event) => {
    setId(event.currentTarget.value);
  };

  const PwHandler = (event) => {
    setPassword(event.currentTarget.value);
  };

  // useEffect(() => {
  //   if (Token !== "") {
  //     navigate("/main");
  //     return;
  //   }

  //   toast({
  //     position: "bottom-right",
  //     title: "잘못된 접근입니다.",
  //     status: "error",
  //     duration: 2000,
  //     isClosable: true,
  //   });
  //   console.log(Token);
  // }, []);

  const handleSubmit = async () => {
    const body = {
      identification: Id,
      password: Password,
    };

    try {
      const response = await axios.post(
        "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/login",
        body
      );
      // sessionStorage.setItem("user_id", response.data.user_id);
      setToken(response.data.token);
      setUserId(response.data.user_id);
      navigate("/main");
      toast({
        position: "bottom-right",
        title: "로그인 성공",
        description: "로그인 되었습니다.",
        status: "success",
        duration: 2000,
        isCloasabl: true,
      });
    } catch (e) {
      toast({
        position: "bottom-right",
        title: "로그인 실패",
        description: e.response.data,
        status: "error",
        duration: 2000,
        isClosable: true,
      });
    }
  };

  const handleSignup = () => {
    navigate("/register");
  };

  const onSubmitClick = (e) => {
    if (e.key === "Enter") {
      handleSubmit();
    }
  };

  return (
    <div style={{ display: "flex", width: "100%", height: "100vh" }}>
      <div // 검정색 배경
        style={{
          width: "75%",
          backgroundColor: "black",
          alignItems: "center",
          display: "flex",
        }}>
        <Carousel
          fade
          style={{
            width: "100%",
          }}>
          <Carousel.Item>
            <img className="d-block w-100" src={sellet} alt="First slide" />
          </Carousel.Item>
          <Carousel.Item>
            <img className="d-block w-100" src={sellet2} alt="Third slide" />
          </Carousel.Item>
        </Carousel>
      </div>
      <div // 흰색 배경
        style={{
          backgroundColor: "white",
          height: "70%",
          width: "25%",
          marginTop: "13%",
        }}>
        <div
          style={{
            width: "88%",
            marginLeft: "6%",
            height: "45%",
          }}>
          <div
            style={{
              fontSize: "40px",
              fontWeight: "bold",
              textAlign: "center",
            }}>
            SELETT
          </div>
          <label style={{ width: "80%", marginLeft: "10%" }}>User Name</label>
          <Input
            placeholder="ID"
            style={{ width: "80%", marginLeft: "10%" }}
            value={Id}
            onChange={IdHandler}
          />
          <br />
          <br />
          <label style={{ width: "80%", marginLeft: "10%" }}>Password</label>
          <br />
          <InputGroup
            size="md"
            style={{ width: "80%", marginLeft: "10%" }}
            onKeyPress={onSubmitClick}>
            <Input
              value={Password}
              onChange={PwHandler}
              pr="4.5rem"
              type={Show ? "text" : "password"}
              placeholder="Enter password"
            />
            <InputRightElement width="4.5rem">
              <Button h="1.75rem" size="sm" onClick={handleClick}>
                {Show ? "Hide" : "Show"}
              </Button>
            </InputRightElement>
          </InputGroup>
          <br />
          <Button
            colorScheme="gray"
            onClick={handleSubmit}
            style={{ width: "80%", marginLeft: "10%" }}
            disabled={Id === "" || Password === ""}>
            Login
          </Button>
          <div
            onClick={handleSignup}
            style={{ marginTop: "10px", cursor: "pointer", marginLeft: "10%" }}>
            Register
          </div>
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
