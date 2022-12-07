import React, { Fragment } from "react";

function GrammerForm(GrammerText) {
  return (
    <Fragment>
      {GrammerText.GrammerText &&
        GrammerText.GrammerText.length !== 0 &&
        GrammerText.GrammerText.map((text, index) => (
          <table // 첫 번째 테이블 시작
            key={index}
            style={{
              width: "90%",
              borderBottom: "1px solid #d9d9d9",
              borderTop: "1px solid #d9d9d9",
              borderRadius: "20px",
              marginTop: "10px",
              marginBottom: "10px",
              justifyContent: "center",
              maxHeight: "100%",
              color: "black",
              // fontWeight: "normal",
              fontSize: "13px",
            }}>
            <tbody>
              <tr
                style={
                  {
                    // borderBottom: "1px solid #d9d9d9"
                  }
                }>
                <th
                  style={{
                    width: "30%",
                    fontWeight: "bold",
                    // borderRight: "1px solid #d9d9d9",
                  }}>
                  입력 내용
                </th>
                <th style={{ width: "70%", color: "#ff6955" }}>
                  {text.orgStr}
                </th>
              </tr>
              <tr
                style={
                  {
                    // borderBottom: "1px solid #d9d9d9"
                  }
                }>
                <th
                  style={{
                    width: "30%",
                    // borderRight: "1px solid #d9d9d9",
                  }}>
                  대치어
                </th>
                <th style={{ width: "70%", color: "#3381ff" }}>
                  {text.candWord}
                </th>
              </tr>
              <tr>
                <th
                  style={{
                    width: "30%",
                    verticalAlign: "top",
                    // borderRight: "1px solid #d9d9d9",
                  }}>
                  도움말
                </th>
                <th style={{ width: "70%", fontWeight: "500" }}>{text.help}</th>
              </tr>
            </tbody>
          </table>
        ))}
    </Fragment>
  );
}

export default GrammerForm;
