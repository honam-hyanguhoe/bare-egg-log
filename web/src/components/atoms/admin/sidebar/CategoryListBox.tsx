import React from 'react';
import styled from 'styled-components';
interface CategoryListBoxProps {
    text:string,
    imageUrl: string,
    isActive: boolean
}

const StyledCategoryListBox = styled.div<{ isActive:boolean }>`
  font-size: 1.2em;
  font-weight: 300;
  font-family: "Line-Seed-Sans-App";
  color: #f9f9f9;
  text-align: center;
  padding: 0.7em;
  border-radius: 0.4em;
  background-color: ${props => props.isActive ? '#2A2A2A' : 'transparent'};
  display: flex;
  align-content: center;
  gap: 0.5em;
  & > .imageBox {
    display: inline-block;
    width: 1.2em;
    height: 1.2em;
  }
  & > .imageBox > img {
    width: 100%;
  }
`;


const CategoryListBox = (data:CategoryListBoxProps) => {
    return <StyledCategoryListBox isActive={data.isActive}>
        <div className={"imageBox"}>
            <img src={data.imageUrl} alt={"icon"}/>
        </div>
        <span>{data.text}</span>
    </StyledCategoryListBox>;
};

export default CategoryListBox;
