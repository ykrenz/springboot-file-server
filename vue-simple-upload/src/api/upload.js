import { request } from '../network/request'

export function merge (form) {
  return request({
    url: '/upload/merge',
    method: 'post',
    data: form
  })
}
