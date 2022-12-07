import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist();

export const CoverState = atom({
  key: "counter",
  default: [],
});

export const folderClickIdState = atom({
  key: "folderClick",
  default: 0,
});

export const CompanyListState = atom({
  key: "CompanyListSt",
  default: [],
});

// export const
export const fileClickIdState = atom({
  key: "fileClick",
  default: 0,
});

export const MemoState = atom({
  key: "MemoSta",
  default: "",
});

export const TokenState = atom({
  key: "Tok",
  default: "",
  effects_UNSTABLE: [persistAtom],
});

export const UserIdState = atom({
  key: "userIdState",
  default: 0,
  effects_UNSTABLE: [persistAtom],
});
