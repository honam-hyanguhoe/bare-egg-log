import BigNumber from "bignumber.js";

export interface Inquiry {
    id: BigNumber,
    title: string,
    content: string,
    isNoted: boolean
};
