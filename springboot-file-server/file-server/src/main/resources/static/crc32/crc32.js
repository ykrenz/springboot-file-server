function Crc32() {
    crc = -1
    table = makeTable();

    this.append = function append(data) {
        let crcTemp = crc;
        for (let offset = 0; offset < data.byteLength; offset++) {
            crcTemp = (crcTemp >>> 8) ^ table[(crcTemp ^ data[offset]) & 0xFF]
        }
        crc = crcTemp;
    }

    this.compute = function compute() {
        return (crc ^ -1) >>> 0
    }

    function makeTable() {
        const table = []
        for (let i = 0; i < 256; i++) {
            let t = i
            for (let j = 0; j < 8; j++) {
                if (t & 1) {
                    // IEEE 标准
                    t = (t >>> 1) ^ 0xEDB88320
                } else {
                    t >>>= 1
                }
            }
            table[i] = t
        }
        return table
    }
}
