import styled, { css } from 'styled-components';
import { MdDone, MdDelete } from 'react-icons/md';
import { instance } from '../api/axios';

function SubItem({ props }) {
  const {id, title, complete, content} = props;

  function onComplateClickHandler(e) {
    instance.put(`/api/v1/todolists/${id}`)
    .then((res) => {
      window.location.replace("/")
    })
    .catch((err) => {
      console.log(err);
    })
  }

  function onDeleteClickHandler(e) {
    instance.delete(`/api/v1/todolists/${id}`)
    .then((res) => {
      window.location.replace("/")
      })
    .catch((err) => {
      console.log(err);
    })
  }

  return (
    <>
    <TodoItemBlock>
        <CheckCircle done={complete} onClick={onComplateClickHandler}>{complete && <MdDone />}</CheckCircle>
        <Box>
          <Title done={complete}>{title}</Title>
          <Content done={complete}>{content}</Content>
        </Box>
        <Remove onClick={onDeleteClickHandler}><MdDelete /></Remove>
      </TodoItemBlock>
    </>
  );
}

export default SubItem;

const Remove = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  color: #dee2e6;
  font-size: 24px;
  cursor: pointer;
  &:hover {
    color: #ff6b6b;
  }
  display: none;
`;

const TodoItemBlock = styled.div`
  width:100%;
  display: flex;
  padding-top: 12px;
  padding-bottom: 12px;
  &:hover {
    ${Remove} {
      display: initial;
    }
  }
`;

const CheckCircle = styled.div`
  width: 32px;
  height: 32px;
  border-radius: 16px;
  border: 1px solid #ced4da;
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  cursor: pointer;
  ${props =>
    props.done &&
    css`
      border: 1px solid #38d9a9;
      color: #38d9a9;
    `}
`;

const Box = styled.div`
  width: 100%;
  align-items: center;
  justify-content: center;

`;

const Title = styled.div`
  flex: 1;
  font-size: 15px;
  font-weight: bold;
  color: #495057;
  ${props =>
    props.done &&
    css`
      color: #ced4da;
    `}
`;

const Content = styled.div`
  flex: 1;
  font-size: 12px;
  color: #495057;
  font-weight: normal;
  ${props =>
    props.done &&
    css`
      color: #ced4da;
    `}
`;
