import React, { useState } from 'react';
import styled, { css } from 'styled-components';
import { MdDone, MdDelete } from 'react-icons/md';
import { instance } from '../api/axios';
import SubItem from './SubItem';

function TodoItem({ id, done, title, content }) {
  const [open, setOpen] = useState(false);
  const [values, setValues] = useState([])

  function onSubItemHandler(e) {
    setOpen(!open);
    instance.get(`/api/v1/todolists/${id}`)
    .then((res) => {
      setValues(res.data.subList)
    }).catch((err) => {
      console.log(err);
    })
  }

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
    <div>
    <TodoItemBlock>
      <CheckCircle done={done} onClick={onComplateClickHandler} >{done && <MdDone/>}</CheckCircle>
      <Box onClick={onSubItemHandler}>
        <Title done={done}>{title}</Title>
        <Content done={done}>{content}</Content>
      </Box>
      <Remove onClick={onDeleteClickHandler}><MdDelete /></Remove>
    </TodoItemBlock>
    {open && (
       values.map((value) => (
        <SubItem key={value.id} id={value.id} title={value.title} done={value.complete} content={value.content} />
      )))}
  </div>
  )
}

export default TodoItem;

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
  display: flex;
  align-items: center;
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
  font-size: 21px;
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
  font-size: 15px;
  color: #495057;
  font-weight: normal;
  ${props =>
    props.done &&
    css`
      color: #ced4da;
    `}
`;
