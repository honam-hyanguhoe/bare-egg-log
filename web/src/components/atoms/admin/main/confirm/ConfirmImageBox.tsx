import React from "react";
import styled from "styled-components";
import { ImageProps } from "@custom-types/ImageProps";
import { Images } from "@custom-types/Images";

const StyleConfirmImageBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1em;
  overflow-scrolling: auto;

  & > img {
    width: 100%;
  }
`;

const ConfirmImageBox = (imageProps: ImageProps) => {
  const images: Images = imageProps.images;
  return (
    <StyleConfirmImageBox>
      <img src={images.imageFirst} alt="사진1" />
      <img src={images.imageSecond} alt="사진2" />
    </StyleConfirmImageBox>
  );
};

export default ConfirmImageBox;
