import BigNumber from "bignumber.js";

export interface User {
    name: string,
    hospital?: string,
    id: BigNumber,
    profileImgUrl?: string,
    email: string,
    createdAt: string
};
