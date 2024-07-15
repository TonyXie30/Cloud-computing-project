<script lang="ts" setup>

import { onMounted, ref } from 'vue';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import { storage } from '@/utils/storage';
//import axios from 'axios';
dayjs.extend(relativeTime);
const props = defineProps<{ eventId: number }>();

// Use eventId in the component as needed


type Comment = {
  author: string;
  avatar: string;
  content: string;
  datetime: string;
};


const comments = ref<Comment[]>([]);
const submitting = ref<boolean>(false);
const value = ref<string>('');


const handleSubmit = () => {
  if (!value.value) {
    return;
  }

  submitting.value = true;

  setTimeout(() => {
    submitting.value = false;
    comments.value = [
      {
        author: storage.get('user')?.name,
        avatar: 'https://k.sinaimg.cn/n/sinakd20110/560/w1080h1080/20230930/915d-f3d7b580c33632b191e19afa0a858d31.jpg/w700d1q75cms.jpg',
        content: value.value,
        datetime: dayjs().fromNow(),
      },
      ...comments.value,
    ];
    value.value = '';
  }, 1000);
  console.log(props.eventId);
  // const apiUrl = 'http://'auth-service:8443'/api/addComment?eventId='+props.eventId+'&comment='+value.value+'&username='+storage.get('user')?.name;
  // axios
  //   .post(apiUrl)
  //   .then(response => {
  //     console.log(response.data.data)
  //     if (response.data.code === 200) {
  //
  //     } else {
  //       console.error('Failed to fetch comments');
  //     }
  //   })
  //   .catch(error => {
  //     console.error(error);
  //   });
};

onMounted(() => {
  console.log(props.eventId);
  // const apiUrl = 'http://'auth-service:8443'/api/showComment?eventId='+props.eventId;
  // axios
  //   .post(apiUrl)
  //   .then(response => {
  //     if (response.data.code === 200) {
  //       const fetchedComments = response.data.data.map((comment: any) => ({
  //         author: comment.user.username,
  //         avatar: comment.user.picture !== undefined && comment.user.picture !== 'null' ? comment.user.picture : 'https://k.sinaimg.cn/n/sinakd20110/560/w1080h1080/20230930/915d-f3d7b580c33632b191e19afa0a858d31.jpg/w700d1q75cms.jpg',
  //         content: comment.content,
  //         datetime: dayjs(comment.create_time).fromNow(),
  //       }));
  //       comments.value = fetchedComments;
  //     } else {
  //       console.error('Failed to fetch comments');
  //     }
  //   })
  //   .catch(error => {
  //     console.error(error);
  //   });
});
</script>

<template>
  <div>
    <a-list
      v-if="comments.length"
      :data-source="comments"
      :header="`${comments.length} ${comments.length > 1 ? 'replies' : 'reply'}`"
      item-layout="horizontal"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <a-comment :author="item.author" :avatar="item.avatar" :content="item.content" :datetime="item.datetime" />
        </a-list-item>
      </template>
    </a-list>
    <a-comment>
<!--      <template #avatar>-->
<!--        <a-avatar src='https://k.sinaimg.cn/n/sinakd20110/560/w1080h1080/20230930/915d-f3d7b580c33632b191e19afa0a858d31.jpg/w700d1q75cms.jpg' alt="Han Solo" />-->
<!--      </template>-->
<!--      <template #content>-->
<!--        <a-form-item>-->
<!--          <a-textarea v-model:value="value" :rows="4" />-->
<!--        </a-form-item>-->
<!--        <a-form-item>-->
<!--          <a-button html-type="submit" :loading="submitting" type="primary" @click="handleSubmit">-->
<!--            Add Comment-->
<!--          </a-button>-->
<!--        </a-form-item>-->
<!--      </template>-->
    </a-comment>
  </div>
</template>

