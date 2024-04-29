import React from 'react';
import styled from 'styled-components';
import CategoryListBox from "../../../atoms/admin/sidebar/CategoryListBox";
import UserCircleLight from "../../../../assets/images/default/user_cicrle_light.svg";
import ChatAltLight from "../../../../assets/images/default/chat_alt_light.svg";

const StyledCategoryList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.2em;
`

const CategoryList = () => {
    return <StyledCategoryList>
        <CategoryListBox imageUrl={UserCircleLight} text={"인증 승인 요청"} isActive={false}/>
        <CategoryListBox imageUrl={ChatAltLight} text={"문의사항"} isActive={true}/>
    </StyledCategoryList>
}
export default CategoryList;
