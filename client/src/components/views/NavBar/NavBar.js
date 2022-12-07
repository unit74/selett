import React, { useState, useEffect } from "react";
import logo_img from "../../../imageFolder/lala1.png";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";

import {
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  Spinner,
  useToast,
} from "@chakra-ui/react";
import {
  TokenState,
  UserIdState,
  CoverState,
  fileClickIdState,
  folderClickIdState,
  CompanyListState,
} from "../MainPage/Atom";
import { HamburgerIcon } from "@chakra-ui/icons";
// import axios from "axios";

function NavBar(props) {
  const navigate = useNavigate();
  const [Main, setMain] = useState(false);
  const toast = useToast();

  const [Token, setToken] = useRecoilState(TokenState);
  const [userId, setuserId] = useRecoilState(UserIdState);
  const [Cover, setCover] = useRecoilState(CoverState);
  const [fileClickId, setfileClickId] = useRecoilState(fileClickIdState);
  const [folderClickId, setfolderClickId] = useRecoilState(folderClickIdState);
  const [CompanyList, setCompanyList] = useRecoilState(CompanyListState);

  const handleAccount = () => {
    // 메뉴 탭에서 마이페이지를 눌렀을 때 실행
    navigate("/account");
  };

  const handleProfile = () => {
    // 메뉴 탭에서 계정관리를 눌렀을 때 실행
    navigate("/profile");
  };

  const navHandler = () => {
    // 로고를 클릭했을 때 메인으로 이동되게 해줌
    navigate("/main");
  };

  const logoutHandler = () => {
    // 로그아웃을 눌렀을 때 실행되는 코드
    toast({
      position: "bottom-right",
      title: "로그아웃 성공",
      description: "로그아웃 되었습니다.",
      status: "success",
      duration: 2000,
      isCloasabl: true,
    });
    navigate("/");

    setToken("");
    setuserId(0);
    setCover([]);
    setfileClickId(0);
    setfolderClickId(0);
    setCompanyList([]);
  };

  return (
    <div
      className="navbar navbar-light bg-light"
      style={{
        boxShadow: "0px 4px 4px -4px black",
        height: "64px",
        paddingLeft: "20px",
      }}>
      <img // 로고 이미지
        src={logo_img}
        onClick={navHandler}
        style={{ width: "100px", height: "40px", cursor: "pointer" }}
        alt=""
      />
      <div // 메인 페이지일 경우에는 로그아웃 버튼과 메뉴 버튼을 띄움
        style={{
          display: "flex",
          alignItems: "center",
        }}>
        {props.Loading ? (
          <Spinner
            thickness="4px"
            speed="0.65s"
            emptyColor="gray.200"
            color="blue.700"
            size="xl"
            marginRight="20px"
          />
        ) : null}
        <button // 로그아웃 버튼
          style={{
            marginRight: "20px",
            width: "min-content",
            // height: "40px",
            fontWeight: "bold",
          }}
          onClick={logoutHandler}>
          logout
        </button>
        <Menu>
          {/* 로그아웃 버튼을 클릭했을 때 메뉴 창이 열리는데, 그 메뉴창 */}
          <MenuButton>
            <HamburgerIcon
              style={{ width: "40px", height: "40px", marginRight: "9px" }}
            />
          </MenuButton>
          <MenuList minWidth="100px" style={{ width: "120px" }}>
            <MenuItem style={{ width: "120px" }} onClick={handleProfile}>
              마이페이지
              {/* 마이페이지로 이동할 수 있도록 함 */}
            </MenuItem>
            <MenuItem style={{ width: "120px" }} onClick={handleAccount}>
              계정관리
              {/* 계정관리 페이지로 이동할 수 있도록 함 */}
            </MenuItem>
          </MenuList>
        </Menu>
      </div>
    </div>
  );
}

export default NavBar;
