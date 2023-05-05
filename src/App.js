import './App.css';
import { createGlobalStyle } from 'styled-components';
import TodoTemplate from './components/TodoTemplate';
import TopHead from './components/TopHead';
import TodoList from './components/TodoList';
import TodoCreate from './components/TodoCreate';


function App() {
  return (
    <div className="App">
      <GlobalStyle />
      <TodoTemplate >
        <TopHead />
        <TodoList />
        <TodoCreate />
      </TodoTemplate> 
    </div>
  );
}


const GlobalStyle = createGlobalStyle`
  body {
    background: #e9ecef;
  }
`;


export default App;
