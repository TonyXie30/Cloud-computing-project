<script setup lang="ts">
import {onMounted, ref} from 'vue';
import EventDetail from '../../event-page/modules/event-detail.vue';
import {Edit, Search} from '@element-plus/icons-vue'
import axios from "axios";
import {storage} from "@/utils/storage";
import { useMessage } from 'naive-ui'

interface Event {
  id: number;
  name: string;
  location: string;
  type: string;
  date: string;
  description: string;
  imageUrl: string;
}
// const searchQuery = ref('');
const openedImgSrc = ref('');
const windowOpen = ref(false);
const message = useMessage()
const events = ref<Event[]>([
  {
    id: 1,
    name: 'Math Conference',
    location: 'Building A',
    type: '学术交流',
    date: '2024.5.1 20:00-22:00',
    description: 'A conference about advanced mathematics.',
    imageUrl: 'https://newshub.sustech.edu.cn/uploads/large/2021/12/01145715_61949.png'
  }
]);


defineOptions({
  name: 'CreativityBanner'
});

onMounted(()=>{
  const apiUrl = 'http://'+ 'a2a127a20ddfa4b7e9442a040151db72-615736446.us-east-1.elb.amazonaws.com:8443' + '/api/main/getRecommendEvents';
  const token = window.localStorage.getItem('jwt');
  axios
    .post(apiUrl, {}, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    })
    .then(response => {
      console.log(response.data.data.length);
      if(response.data.code===200){
        events.value = response.data.data.map((event: any) => ({
          id: event.id,
          name: event.name,
          location: event.place.name,
          type: '未知类型', // 你可以根据具体情况设置类型
          date: new Date(event.startTime).toLocaleString() + '-' + new Date(event.endTime).toLocaleString(),
          description: event.description,
          imageUrl: event.picture
        }));
      }else {
        console.log("Failed to get events")
      }
    })
    .catch(error => {
      console.error(error);
    });
})

function openModal(src: string) {
  windowOpen.value = true;
  openedImgSrc.value = src;
}

function bookEvent(id: number) {
  const apiUrl = 'http://'+ 'a2a127a20ddfa4b7e9442a040151db72-615736446.us-east-1.elb.amazonaws.com:8443' + '/api/main/joinEvent?username='+storage.get('user')?.name+'&eventId='+id;
  const token = window.localStorage.getItem('jwt');
  axios
    .post(apiUrl, {}, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    })
    .then(response => {
      console.log(response.data);
      if(response.data.code===200){
        console.log("Event booked successfully")
        message.success("预约成功！")
      }else {
        console.log("Failed to book event")
        message.warning("预约失败😭")
      }
    })
    .catch(error => {
      console.error(error);
      message.error("出错了❌")
    });
}

</script>

<template>
  <NCard title="活动推荐" :bordered="false" size="small" class="h-full card-wrapper">

    <div v-for="event in events" :key="event.id" class="event-card">
        <div class="event-info">
          <h2>{{ event.name }}</h2>
          <p>活动类别: {{ event.type }}</p>
          <p>活动地点: {{ event.location }}</p>
          <p>活动时间：{{ event.date }}</p>
          <el-button type="primary" :icon="Search" size="large" @click="openModal(event.imageUrl)">活动描述</el-button>
          <el-button type="primary" :icon="Edit" size="large" @click="bookEvent(event.id)">活动预约</el-button>
        </div>

        <p></p>

        <div class="event-image">
          <img :src="event.imageUrl" alt="Event image" />
        </div>
      </div>
      <EventDetail v-if="windowOpen" @close-window="windowOpen = false" :image-src="openedImgSrc"></EventDetail>

  </NCard>
</template>

<style scoped>
.event-image {
  margin-top: 20px; /* Adjust this value as needed */
  /* Other styles... */
}
</style>
