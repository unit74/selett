import React from "react";
import { HamburgerIcon } from "@chakra-ui/icons";

function HiddenTag(props) {
  const HiddenHandler = () => {
    props.refreshfunction(props.ListToggle);
  };

  return (
    <div
      style={{
        marginTop: "20px",
        backgroundColor: "#303136",
        color: "white",
        height: "35px",
        width: "20px",
        overflow: "hidden",
        fontSize: "13px",
        cursor: "pointer",
        display: "flex",
        padding: "0.1%",
        textAlign: "center",
        border: "1px solid black",
        // marginTop: "50px",
        marginRight: "1%",
        fontWeight: "bold",
        fontSize: "15px",
        borderTopRightRadius: "10px",
        borderBottomRightRadius: "10px",
      }}
      onClick={HiddenHandler}>
      <HamburgerIcon w={4} h={4} style={{ marginTop: "7px" }} />
    </div>
  );
}

export default HiddenTag;
