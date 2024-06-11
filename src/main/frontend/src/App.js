import logo from './logo.svg';
import React, { useEffect, useState } from 'react';

import './App.css';

const DataStream = ({ type }) => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource(`http://localhost:8080/sse/${type}`);
    const batchSize = 100; // 한 번에 처리할 데이터의 크기
    const maxItems = 1000;
    let buffer = [];
    eventSource.onmessage = (event) => {
      // const parsedData = parseData(event.data);
      // setData((prevData) => [...prevData, event.data]);
      buffer.push(event.data);
      if (buffer.length >= batchSize) {
        setData((prevData) => {
          const newData = [...prevData, ...buffer];
          return newData.length > maxItems ? newData.slice(newData.length - maxItems) : newData;
        });
        buffer = [];
      }
    };

    return () => {
      eventSource.close();
    };
  }, [type]);

  // 주어진 문자열을 파싱하여 객체로 변환하는 함수
  const parseData = (dataString) => {
    const regex = /([^:]+) : ([^,]+)/g;
    const parsedData = {};
    let match;

    while ((match = regex.exec(dataString)) !== null) {
      const key = match[1].trim();
      const value = match[2].trim();
      parsedData[key] = value;
    }

    return parsedData;
  };
  // const parseData = (dataString) => {
  //   const parts = dataString.split(", ");
  //   const parsedData = {};
  //   parts.forEach(part => {
  //     const [key, value] = part.split(" : ");
  //     parsedData[key.trim()] = value.trim();
  //   });
  //   return parsedData;
  // };

  return (
      <div className="data-stream">
        <h3>Stream {type}</h3>
        <ul>
          {data.map((item, index) => (
              <li key={index}>
                {/* 파싱된 객체의 속성을 사용하여 데이터를 표시합니다. */}
                {/* 예: "ThreadName", "PoolSize", "Message" 등 */}
                {/*ThreadName: {item.ThreadName}, PoolSize: {item.PoolSize}, Message: {item.Message}*/}
                {item}
              </li>
          ))}
        </ul>
      </div>
  );
};

function App() {
  return (
      <div className="App">
        {[...Array(10).keys()].map((type) => (
            <DataStream key={type+1} type={type+1} />
        ))}
      </div>
  );

  // return (
  //   <div className="App">
  //     <header className="App-header">
  //       <img src={logo} className="App-logo" alt="logo" />
  //       <p>
  //         Edit <code>src/App.js</code> and save to reload.
  //       </p>
  //       <a
  //         className="App-link"
  //         href="https://reactjs.org"
  //         target="_blank"
  //         rel="noopener noreferrer"
  //       >
  //         Learn React
  //       </a>
  //     </header>
  //   </div>
  // );
}

export default App;
