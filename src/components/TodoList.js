import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { instance } from '../api/axios';
import TodoItem from './TodoItem';

function TodoList() {
  const [values, setValues] = useState([])
  const [page, setPage] = useState(0)
  const [isFetching, setFetching] = useState(false)
  const [hasNextPage, setNextPage] = useState(true)

  const dataFetch = () => {
    instance.get(`/api/v1/todolists?page=${page}&size=5`)
    .then((res) => {
      console.log(res.data);
      setValues([...values, ...(res.data.content)])
      setPage((page) => page + 1)
      setNextPage(!res.data.last)
      setFetching(false)
    })
    .catch((err) => {console.log(err)});
  };

  useEffect(() => {
    const handleScroll = () => {
      const { scrollTop, offsetHeight } = document.documentElement
      if (window.innerHeight + scrollTop >= offsetHeight) {
        setFetching(true)
      }
    }
    setFetching(true)
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])
  
  useEffect(() => {
    if (isFetching && hasNextPage) dataFetch()
    else if (!hasNextPage) setFetching(false)
  }, [isFetching])


  return <TodoListBlock>
    {values.map((value) => (
       <TodoItem key={value.id} id={value.id} title={value.title} done={value.complete} content={value.content} />
     ))}
  </TodoListBlock>
}

export default TodoList;


const TodoListBlock = styled.div`
  flex: 1;
  padding: 20px 32px;
  padding-bottom: 48px;
  overflow-y: auto;
  background: white; /* 사이즈 조정이 잘 되고 있는지 확인하기 위한 임시 스타일 */
`;
