<template>
  <uploader
    :options="options"
    :autoStart="true"
    :file-status-text="fileStatusText"
    class="uploader-example"
    @file-added="onFileAdded"
    @file-success="onFileSuccess"
    @file-complete="onfileComplete"
  >
    <uploader-unsupport></uploader-unsupport>
    <uploader-drop>
      <p>请选择需要上传的文件</p>
      <uploader-btn>select files</uploader-btn>
      <uploader-btn :attrs="attrs">select images</uploader-btn>
      <uploader-btn :directory="true">select folder</uploader-btn>
    </uploader-drop>
    <uploader-list></uploader-list>
  </uploader>

</template>

<script>
import SparkMD5 from 'spark-md5'
import { merge } from '../api/upload'
export default {
  data () {
    return {
      options: {
        target: '///localhost:3000/upload/chunk',
        chunkSize: 1024 * 1024,
        testChunks: true,
        checkChunkUploadedByResponse: function (chunk, message) {
          const objMessage = JSON.parse(message)
          if (objMessage.data.exist) {
            // 秒传
            // console.log('秒传' + objMessage.data.file)
            return true
          }
          const chunkNumbers = objMessage.data.chunkNumbers
          return (chunkNumbers || []).indexOf(chunk.offset + 1) >= 0
        },
        // generateUniqueIdentifier: function (file) {
        //   console.log('sdfasdf')
        //   const chunkSize = 10 * 1024 * 1000
        //   const data = await md5(file, chunkSize).then(value => {
        //     console.log(value)
        //   })
        //   console.log(data)
        //   return data
        // },
        query (file) {
          return { md5: file.uniqueIdentifier }
        }
      },
      fileStatusText (status, response) {
        const statusTextMap = {
          success: '上传成功',
          error: '上传失败',
          uploading: '上传中',
          paused: '暂停中',
          waiting: '等待中'
        }
        console.log('status=' + status)
        console.log('fileStatusText=' + JSON.stringify(response))
        // 分片合并完成
        if (status === 'merge') {
          console.log('merge')
          return '校验文件中'
        }
        return statusTextMap[status]
        // if (status === 'success' || status === 'error') {
        //   // 只有status为success或者error的时候可以使用 response
        //   // eg:
        //   // return response data ?
        //   return response.data
        // } else {
        //   return statusTextMap[status]
        // }
      },
      attrs: {
        accept: 'image/*'
      }
    }
  },
  methods: {
    onFileAdded (file) {
      // this.options.query.fileType = file.fileType
      // this.options.query.extension = file.getExtension()
      // 计算MD5，下文会提到
      console.log(file)
      this.computeMD5(file)
    },
    onFileSuccess (rootFile, file, response, chunk) {
      // console.log('rootFile', rootFile)
      // console.log('file', file)
      // console.log('chunk', chunk)
      console.log('onFileSuccess')
      const res = JSON.parse(response)
      console.log(res)
      console.log(res.data.exist)
      if (res.code === 200 && !res.data.exist) {
        this.fileStatusText('merge')
        // 合并
        const form = new FormData()
        form.append('md5', file.uniqueIdentifier)
        form.append('filename', file.name)
        form.append('size', file.size)
        // form.append('fileType', file.getType())
        // form.append('extension', file.getExtension())
        merge(form).then(response => {
          console.log('合并结果')
          console.log(response.data)
          console.log(response.data.code)
        })
      }
    },
    onfileComplete (rootFile) {
      console.log('完毕')
    },

    /**
     * 计算md5，实现断点续传及秒传
     * @param file
     */
    computeMD5 (file) {
      // 大文件的md5计算时间比较长，显示个进度条
      const fileReader = new FileReader()
      const time = new Date().getTime()
      const blobSlice =
        File.prototype.slice ||
        File.prototype.mozSlice ||
        File.prototype.webkitSlice
      let currentChunk = 0
      const chunkSize = 10 * 1024 * 1000
      const chunks = Math.ceil(file.size / chunkSize)
      const spark = new SparkMD5.ArrayBuffer()

      file.pause()
      loadNext()

      fileReader.onload = e => {
        spark.append(e.target.result)
        if (currentChunk < chunks) {
          currentChunk++
          loadNext()
          // 实时展示MD5的计算进度
          this.$nextTick(() => {
            $(`.myStatus_${file.id}`).text('正在校验MD5 ' + ((currentChunk / chunks) * 100).toFixed(0) + '%')
          })
        } else {
          const md5 = spark.end()
          this.computeMD5Success(md5, file)
          console.log(
            `MD5计算完毕：${file.name} \nMD5：${md5} \n分片：${chunks} 大小:${
              file.size
            } 用时：${new Date().getTime() - time} ms`
          )
        }
      }
      fileReader.onerror = function () {
        this.error(`文件${file.name}读取出错，请检查该文件`)
        file.cancel()
      }
      function loadNext () {
        const start = currentChunk * chunkSize
        const end =
          start + chunkSize >= file.size ? file.size : start + chunkSize
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end))
      }
    },

    computeMD5Success (md5, file) {
      file.uniqueIdentifier = md5 // 把md5值作为文件的识别码
      file.resume() // 开始上传
    }
  }
}

// function md5 (file, chunkSize) {
//   return new Promise((resolve, reject) => {
//     const blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice
//     const chunks = Math.ceil(file.size / chunkSize)
//     let currentChunk = 0
//     const spark = new SparkMD5.ArrayBuffer()
//     const fileReader = new FileReader()
//     fileReader.onload = function (e) {
//       spark.append(e.target.result)
//       currentChunk++
//       if (currentChunk < chunks) {
//         loadNext()
//       } else {
//         const md5 = spark.end()
//         resolve(md5)
//       }
//     }
//     fileReader.onerror = function (e) {
//       reject(e)
//     }
//     function loadNext () {
//       const start = currentChunk * chunkSize
//       let end = start + chunkSize
//       if (end > file.size) {
//         end = file.size
//       }
//       fileReader.readAsArrayBuffer(blobSlice.call(file, start, end))
//     }
//     loadNext()
//   })
// }
</script>
<style >
.uploader-example {
  width: 880px;
  padding: 15px;
  margin: 40px auto 0;
  font-size: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.4);
}
.uploader-example .uploader-btn {
  margin-right: 4px;
}
.uploader-example .uploader-list {
  max-height: 440px;
  overflow: auto;
  overflow-x: hidden;
  overflow-y: auto;
}
 /* 隐藏上传按钮 */
/* .uploader-file-actions {
  display:none;
        position: absolute;
        clip: rect(0, 0, 0, 0);
} */
</style>
