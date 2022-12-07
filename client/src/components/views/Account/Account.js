import "./Account.css";
import { useRecoilState } from "recoil";
import NavBar from "../NavBar/NavBar";
import {
  Button,
  Input,
  InputGroup,
  InputRightElement,
  useToast,
  Box,
} from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  TokenState,
  UserIdState,
  CoverState,
  CompanyListState,
  fileClickIdState,
  folderClickIdState,
} from "../MainPage/Atom";
// import bg from "./background3.PNG";
import bg from "../../../imageFolder/background.png";

function Account() {
  const navigate = useNavigate();
  const [CurrentPassword, setCurrentPassword] = useState("");
  const [NewPassword, setNewPassword] = useState("");
  const [NewPasswordCheck, setNewPasswordCheck] = useState("");
  const [Show, setShow] = useState(false);
  const [Show_c, setShow_c] = useState(false);
  const [CompanyList, setCompanyList] = useRecoilState(CompanyListState);
  const [Show_cr, setShow_cr] = useState(false);
  const [fileClickId, setfileClickId] = useRecoilState(fileClickIdState);
  const [folderClickId, setfolderClickId] = useRecoilState(folderClickIdState);
  const [Token, setToken] = useRecoilState(TokenState);
  const [Cover, setCover] = useRecoilState(CoverState);
  const [UserId, setUserId] = useRecoilState(UserIdState);
  const handleClick = () => setShow(!Show);
  const handleClick_c = () => setShow_c(!Show_c);
  const handleClick_cr = () => setShow_cr(!Show_cr);
  const toast = useToast();
  const CurrentPasswordHandler = (event) => {
    setCurrentPassword(event.currentTarget.value);
  };

  const NewPasswordHandler = (event) => {
    setNewPassword(event.currentTarget.value);
  };

  const NewPasswordCheckHandler = (event) => {
    setNewPasswordCheck(event.currentTarget.value);
  };

  // const passwordHandler = () => {
  //   if (NewPassword === NewPasswordCheck) {
  //     setCurrentPassword(NewPassword);
  //   }
  // };
  useEffect(() => {
    setCover([]);
    setfileClickId(0);
    setfolderClickId(0);
    setCompanyList([]);
  }, []);

  const passwordHandler = async () => {
    const body = {
      current_password: CurrentPassword,
      new_password: NewPassword,
      user_id: UserId,
    };
    // console.log(body);

    try {
      const response = await axios.put(
        "http://ec2-13-209-139-191.ap-northeast-2.compute.amazonaws.com/register/password",
        body,
        {
          headers: {
            Authorization: Token,
          },
        }
      );
      // console.log(response);
      toast({
        //현재 비밀번호 실패 toast
        position: "bottom-right",
        title: "다시 로그인 해주세요.",
        status: "error",
        duration: 2000,
        isClosable: true,
      });
      navigate("/");
    } catch (e) {
      toast({
        //현재 비밀번호 실패 toast
        position: "bottom-right",
        title: "비밀번호 변경 실패",
        description: e.response.data,
        status: "error",
        duration: 2000,
        isClosable: true,
      });
    }
  };
  //newpassword를 currentpassword로 보내기
  return (
    <Box
      className="account_page"
      style={{
        height: "100vh",
        backgroundImage: `url(${bg})`,
        backgroundPosition: "top center",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundAttachment: "fixed",
      }}
      sx={{ "::-webkit-scrollbar": { display: "none" }, overflow: "hidden" }}>
      <div className="account">
        <div>
          <NavBar />
          <Box
            height="min-content"
            fontSize="xx-large"
            textAlign="center"
            marginTop="6%"
            fontWeight="900"
            style={{ fontSize: "50px" }}>
            SELETT
          </Box>
          <div
            className="account_border" //account 테두리
            style={{
              marginTop: "1%",
              marginLeft: "37%",
              width: "27%",
              height: "65%",
              border: "1px solid #5a5a5a",
              borderRadius: "20px",
            }}>
            <div
              className="account"
              style={{
                marginTop: "5px",
                display: "flex",
                marginLeft: "30%",
              }}></div>
            <label
              style={{
                width: "80%",
                marginLeft: "13%",
                marginTop: "5%",
                marginBottom: "1%",
                fontWeight: "bold",
              }}>
              {/* 비밀번호 변경 폼
      현재 비밀번호 확인*/}
              현재 비밀번호
            </label>
            <InputGroup size="md" style={{ width: "80%", marginLeft: "13%" }}>
              <Input
                value={CurrentPassword}
                onChange={CurrentPasswordHandler}
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
            {/*새 비밀번호*/}
            <label
              style={{
                width: "80%",
                marginLeft: "13%",
                marginTop: "3%",
                fontWeight: "bold",
                marginBottom: "1%",
              }}>
              새 비밀번호
            </label>
            <InputGroup size="md" style={{ width: "80%", marginLeft: "13%" }}>
              <Input
                value={NewPassword}
                onChange={NewPasswordHandler}
                pr="4.5rem"
                type={Show_c ? "text" : "password"}
                placeholder="Enter newpassword"
              />
              <InputRightElement width="4.5rem">
                <Button h="1.75rem" size="sm" onClick={handleClick_c}>
                  {Show_c ? "Hide" : "Show"}
                </Button>
              </InputRightElement>
            </InputGroup>
            <label
              style={{
                width: "80%",
                marginLeft: "13%",
                marginTop: "3%",
                fontWeight: "bold",
                marginBottom: "1%",
              }}>
              {/* 새 비밀번호 확인 */}새 비밀번호 확인
            </label>
            <br />
            {/* 비밀번호 확인 시작 */}
            {NewPassword === NewPasswordCheck ? (
              <InputGroup size="md" style={{ width: "80%", marginLeft: "13%" }}>
                <Input
                  value={NewPasswordCheck}
                  onChange={NewPasswordCheckHandler}
                  pr="4.5rem"
                  type={Show_cr ? "text" : "password"}
                  placeholder="Enter newpassword"
                />
                <InputRightElement width="4.5rem">
                  <Button h="1.75rem" size="sm" onClick={handleClick_cr}>
                    {Show_cr ? "Hide" : "Show"}
                  </Button>
                </InputRightElement>
              </InputGroup>
            ) : (
              <div>
                <InputGroup
                  size="md"
                  style={{
                    width: "80%",
                    marginLeft: "13%",
                    border: "red",
                  }}>
                  <Input
                    value={NewPasswordCheck}
                    onChange={NewPasswordCheckHandler}
                    pr="4.5rem"
                    type={Show_cr ? "text" : "password"}
                    placeholder="Enter Newpassword"
                    // style={{ backgroundColor: "red" }}
                  />
                  <InputRightElement width="4.5rem">
                    <Button h="1.75rem" size="sm" onClick={handleClick_cr}>
                      {Show_cr ? "Hide" : "Show"}
                    </Button>
                  </InputRightElement>
                </InputGroup>
                <div
                  style={{
                    marginLeft: "13%",
                    color: "red",
                    fontStyle: "italic",
                    fontSize: "15px",
                  }}>
                  일치하지 않습니다.
                </div>
              </div>
            )}
            <div className="password_confirm">
              <Button
                variant="outline"
                spacing="6"
                style={{
                  borderRadius: "30px",
                  width: "80%",
                  marginLeft: "13%",
                  marginTop: "3%",
                  display: "flex",
                  marginBottom: "10%",
                  marginLeft: "13%",
                }}
                // 변경 버튼 활성화 비활성화
                onClick={passwordHandler}
                colorScheme="blue"
                disabled={
                  CurrentPassword === "" ||
                  NewPassword === "" ||
                  NewPasswordCheck === "" ||
                  NewPassword !== NewPasswordCheck
                  //CurrentPassword확인
                }>
                변경
              </Button>
            </div>
          </div>
        </div>
      </div>
    </Box>
  );
}
export default Account;
