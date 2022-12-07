import React from "react";

function MemoTag({ MemoToggle, setMemoToggle, Grammer, setGrammer }) {
  const memoHandler = () => {
    setMemoToggle(!MemoToggle);
    Grammer && setGrammer(!Grammer);
  };

  return (
    <div
      style={{
        backgroundColor: "#303136",
        color: "white",
        height: "min-content",
        width: "100%",
        marginTop: "50px",
        overflow: "hidden",
        fontSize: "13px",
        cursor: "pointer",
        padding: "0.1%",
        textAlign: "center",
      }}
      onClick={memoHandler}>
      메모장
    </div>
  );
}

export default MemoTag;
