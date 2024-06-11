self.onmessage = function (e) {
  const { data, batchSize } = e.data;
  // 데이터 배치 처리 로직
  const processedData = processData(data, batchSize);
  self.postMessage(processedData);
};

function processData(data, batchSize) {
  // 데이터 처리 로직
  return data.slice(0, batchSize);
}