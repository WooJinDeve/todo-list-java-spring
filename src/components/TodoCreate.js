import React, { useState } from 'react';
import styled, { css } from 'styled-components';
import { MdAdd } from 'react-icons/md';
import { instance } from '../api/axios';

function TodoCreate() {
  const [open, setOpen] = useState(false);
  const [title, setTitle] = React.useState("");
  const [content, setCotnent] = React.useState("");

  function onToggleHandler(e) {
    setOpen(!open);
    setTitle("");
    setCotnent("");
  }
 
  function handleTitleInputChange(e) {
    const titleValue = e.target.value;
    setTitle(titleValue);
  }

  function handleContentInputChange(e) {
    const contentValue = e.target.value;
    setCotnent(contentValue);
  }

  function onCreateClickHandler(e) {
    if (title === "" || content === "") {
        alert("빈 문자는 저장할 수 없습니다.");
        return;
    }

    instance.post(`/api/v1/todolists`, {
        title: title, content: content, subListRequests : [{"title" : "test", "content" : "text"}, {"title" : "test", "content" : "text"}]
      }).then((res) => {
        alert("등록이 완료되었습니다.");
        window.location.replace("/")
      })
    .catch((err) => {
      console.log(err);
    }).finally(() => {
        onToggleHandler(e)
    });
  }

  return (
    <>
      {open && (
        <InsertFormPositioner>
          <InsertForm>
            <Input autoFocus placeholder="제목을 입력" value={title} onChange={handleTitleInputChange}/>
            <Input autoFocus placeholder="내용을 입력" value={content} onChange={handleContentInputChange} />
            <Button onClick={onCreateClickHandler}>추가</Button>
          </InsertForm>
        </InsertFormPositioner>
      )}
      <CircleButton onClick={onToggleHandler} open={open}>
        <MdAdd />
      </CircleButton>
    </>
  );
}

export default TodoCreate;

const CircleButton = styled.button`
  background: #38d9a9;
  &:hover {
    background: #63e6be;
  }
  &:active {
    background: #20c997;
  }

  z-index: 5;
  cursor: pointer;
  width: 80px;
  height: 80px;
  display: block;
  align-items: center;
  justify-content: center;
  font-size: 60px;
  position: absolute;
  left: 50%;
  bottom: 0px;
  transform: translate(-50%, 50%);
  color: white;
  border-radius: 50%;
  border: none;
  outline: none;
  display: flex;
  align-items: center;
  justify-content: center;

  transition: 0.125s all ease-in;
  ${props =>
    props.open &&
    css`
      background: #ff6b6b;
      &:hover {
        background: #ff8787;
      }
      &:active {
        background: #fa5252;
      }
      transform: translate(-50%, 50%) rotate(45deg);
    `}
`;

const InsertFormPositioner = styled.div`
  width: 100%;
  bottom: 0;
  left: 0;
  position: absolute;
`;

const InsertForm = styled.form`
  background: #f8f9fa;
  padding-left: 32px;
  padding-top: 32px;
  padding-right: 32px;
  padding-bottom: 72px;

  border-bottom-left-radius: 16px;
  border-bottom-right-radius: 16px;
  border-top: 1px solid #e9ecef;
`;

const Input = styled.input`
  padding: 12px;
  border-radius: 4px;
  border: 1px solid #dee2e6;
  width: 100%;
  outline: none;
  font-size: 18px;
  box-sizing: border-box;
  margin: 4px;
`;

const Button = styled.div`
    background: #87CEEB;
    color: white;
    padding: 12px;
    border-radius: 4px;
    border: 1px solid #dee2e6;
    width: 100%;
    outline: none;
    font-size: 18px;
    box-sizing: border-box;
    margin: 4px;
`;