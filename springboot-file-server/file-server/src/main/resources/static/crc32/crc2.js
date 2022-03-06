// function Crc32() {
//     crc = -1
//     table = makeTable();
//
//     this.append = function append(data) {
//         let crcTemp = crc;
//         for (let offset = 0; offset < data.byteLength; offset++) {
//             crcTemp = (crcTemp >>> 8) ^ table[(crcTemp ^ data[offset]) & 0xFF]
//         }
//         crc = crcTemp;
//     }
//
//     this.compute = function compute() {
//         return (crc ^ -1) >>> 0
//     }
//
//     function makeTable() {
//         const table = []
//         for (let i = 0; i < 256; i++) {
//             let t = i
//             for (let j = 0; j < 8; j++) {
//                 if (t & 1) {
//                     // IEEE 标准
//                     t = (t >>> 1) ^ 0xEDB88320
//                 } else {
//                     t >>>= 1
//                 }
//             }
//             table[i] = t
//         }
//         return table
//     }
// }
//
//
// /**
//  * @param file 文件
//  * @param chunkSize 分片大小
//  * @returns Promise
//  */
// function Crc32file(file, chunkSize) {
//     return new Promise((resolve, reject) => {
//         let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
//         let chunks = Math.ceil(file.size / chunkSize);
//         let currentChunk = 0;
//         let crc = new Crc32();
//         let fileReader = new FileReader();
//
//         fileReader.onload = function (e) {
//             crc.append(new Uint8Array(e.target.result));
//             currentChunk++;
//             if (currentChunk < chunks) {
//                 loadNext();
//             } else {
//                 let crc32 = crc.compute();
//                 console.info("crcfile",crc32);
//                 resolve(crc32);
//             }
//         };
//
//         fileReader.onerror = function (e) {
//             reject(e);
//         };
//
//         function loadNext() {
//             let start = currentChunk * chunkSize;
//             let end = start + chunkSize;
//             if (end > file.size) {
//                 end = file.size;
//             }
//             fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
//         }
//
//         loadNext();
//     });
// }
