import React from "react";

function GrammerTag({ Grammer, setGrammer, MemoToggle, setMemoToggle }) {
  const grammerHandler = () => {
    setGrammer(!Grammer);
    MemoToggle && setMemoToggle(!MemoToggle);
  };

  return (
    <div
      style={{
        backgroundColor: "#303136",
        color: "white",
        height: "min-content",
        width: "100%",
        marginTop: "50px",
        marginRight: "1%",
        overflow: "hidden",
        fontSize: "13px",
        cursor: "pointer",
        padding: "0.1%",
        textAlign: "center",
      }}
      onClick={grammerHandler}>
      맞춤법검사
    </div>
  );
}

export default GrammerTag;
