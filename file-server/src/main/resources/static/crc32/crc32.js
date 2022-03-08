function Crc32() {
    this.crc = -1;
    this.table = makeTable();

    this.append = function (data) {
        let crc = this.crc;
        for (let offset = 0; offset < data.byteLength; offset++) {
            crc = (crc >>> 8) ^ this.table[(crc ^ data[offset]) & 0xFF]
        }
        this.crc = crc;
    }

    this.compute = function () {
        return (this.crc ^ -1) >>> 0
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

