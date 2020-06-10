<template>
  <uploader
    :options="options"
    :autoStart="false"
    :file-status-text="statusText"
    class="uploader-example"
    @file-added="onFileAdded"
    @file-success="onFileSuccess"
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
        target: '//localhost:3000/api/upload/chunk',
        chunkSize: 1024 * 1024,
        testChunks: true,
        checkChunkUploadedByResponse: function (chunk, message) {
          const objMessage = JSON.parse(message)
          console.log(objMessage)
          if (objMessage.data.uploaded) {
            return true
          }
          const chunkNumbers = objMessage.data.chunkNumbers
          return (chunkNumbers || []).indexOf(chunk.offset + 1) >= 0
        },
        query: {
          fileType: '',
          extension: ''
        }
      },
      statusText: {
        success: '上传成功',
        error: '上传失败',
        uploading: '上传中',
        paused: '暂停中',
        waiting: '等待中'
      },
      attrs: {
        accept: 'image/*'
      }
    }
  },
  methods: {
    onFileAdded (file) {
      this.options.query.fileType = file.fileType
      this.options.query.extension = file.getExtension()
      // this.panelShow = true
      // 计算MD5，下文会提到
      this.computeMD5(file)
    },
    onFileSuccess (rootFile, file, response, chunk) {
      const res = JSON.parse(response)
      if (res.data.merge & res.code === 200 & file.chunks.length > 1) {
        const form = new FormData()
        form.append('identifier', file.uniqueIdentifier)
        form.append('filename', file.name)
        form.append('filesize', file.size)
        form.append('fileType', file.getType())
        form.append('extension', file.getExtension())
        merge(form).then(response => {
          console.log(response)
        })
      } else {
        console.log(res.message)
      }
    },

    /**
     * 计算md5，实现断点续传及秒传
     * @param file
     */
    computeMD5 (file) {
      // 大文件的md5计算时间比较长，显示个进度条
      // const loading = this.$loading({
      //   lock: true,
      //   text: '正在计算MD5',
      //   spinner: 'el-icon-loading',
      //   background: 'rgba(0, 0, 0, 0.7)'
      // })
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

      // 文件状态设为"计算MD5"
      // this.statusSet(file.id, 'md5')
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
          // loading.close()
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
        // loading.close()
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
      // this.statusRemove(file.id)
    }
    /**
             * 新增的自定义的状态: 'md5'、'transcoding'、'failed'
             * @param id
             * @param status
             */
    // statusSet (id, status) {
    //   id = id + 2
    //   const statusMap = {
    //     md5: {
    //       text: '校验MD5',
    //       bgc: '#fff'
    //     },
    //     merging: {
    //       text: '合并中',
    //       bgc: '#e2eeff'
    //     },
    //     transcoding: {
    //       text: '转码中',
    //       bgc: '#e2eeff'
    //     },
    //     failed: {
    //       text: '上传失败',
    //       bgc: '#e2eeff'
    //     }
    //   }
    //   this.$nextTick(() => {
    //     $(`<p class="myStatus_${id}"></p>`).appendTo(`.file_${id} .uploader-file-status`).css({
    //       position: 'absolute',
    //       top: '0',
    //       left: '0',
    //       right: '0',
    //       bottom: '0',
    //       zIndex: '1',
    //       backgroundColor: statusMap[status].bgc
    //     }).text(statusMap[status].text)
    //   })
    // },
    // statusRemove (id) {
    //   this.$nextTick(() => {
    //     $(`.myStatus_${id}`).remove()
    //   })
    // }
  }
}
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
