import { request } from '../network/request'

export function completeMultipart (form) {
  return request({
    url: '/completeMultipart',
    method: 'post',
    data: form
  })
}
