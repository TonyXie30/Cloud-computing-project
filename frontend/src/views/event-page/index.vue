<script setup lang="ts">

import {computed, onMounted, ref} from 'vue';
import EventDetail from './modules/event-detail.vue';
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
const searchQuery = ref('');
const openedImgSrc = ref('');

const desc=ref('');
let eid =ref(0);
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

  },
  {
    id: 2,
    name: 'Literature Workshop',
    location: 'Building B',
    type: '工作坊',
    date: '2024.5.1 20:00-22:00',
    description: 'Explore modern literature themes.',

    imageUrl: 'https://newshub.sustech.edu.cn/uploads/large/2020/07/21222930_21544.png'

  },
  {
    id: 3,
    name: 'Physics Symposium',
    location: 'Building C',
    type: '学术交流',
    date: '2024.5.1 20:00-22:00',
    description: 'Discussion on particle physics.',

    imageUrl: 'https://newshub.sustech.edu.cn/uploads/large/2020/11/171605602063108498.png'

  }
]);
const windowOpen = ref(false);

// Computed property to filter events based on the search query
const filteredEvents = computed(() => {
  if (!searchQuery.value) {
    return events.value;
  }
  return events.value.filter(event => event.name.includes(searchQuery.value));
});

// Method to handle search input, you can add debounce or more complex logic here
function searchEvents() {
  // Intentionally left blank for this example
  // The actual search logic is handled by the computed property above
}

onMounted(()=>{
  const apiUrl = 'http://'+ 'auth-service:8443' + '/api/main/showAllEvents';
  const token = window.localStorage.getItem('jwt');
  axios
    .post(apiUrl, {}, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    })
    .then(response => {
      console.log(response.data.data.content.length);
      if(response.data.code===200){
        events.value = response.data.data.content.map((event: any) => ({
          id: event.id,
          name: event.name,
          location: event.place.name,
          type: '公开活动', // 你可以根据具体情况设置类型
          date: new Date(event.startTime).toLocaleString() + '-' + new Date(event.endTime).toLocaleString(),
          description: event.description,
          imageUrl: event.picture
        }));
      }else {

      }
    })
    .catch(error => {
      console.error(error);
    });
})


function openModal(src: string, des: string, id: number) {
  windowOpen.value = true;
  openedImgSrc.value = src;
  desc.value = des;
  eid.value = id;
}

function bookEvent(id: number) {
  const apiUrl = 'http://' + 'auth-service:8443'+ '/api/main/joinEvent?username='+storage.get('user')?.name+'&eventId='+id;
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

        message.success("预约成功！")
      }else {
        message.warning("预约失败！")
      }
    })
    .catch(error => {
      console.error(error);
    });
}

</script>

<template>
  <div class="search-page">
    <h1>校园活动搜索</h1>
    <input v-model="searchQuery" placeholder="输入您感兴趣的活动，例如“草地音乐节”" @input="searchEvents" />
    <div v-if="events.length > 0" class="events-container">
      <div v-for="event in filteredEvents" :key="event.id" class="event-card">
        <div class="event-info">
          <h2>{{ event.name }}</h2>
          <p>活动类别: {{ event.type }}</p>
          <p>活动地点: {{ event.location }}</p>
          <p>活动时间：{{ event.date }}</p>

          <el-button type="primary" :icon="Search" size="large" @click="openModal(event.imageUrl, event.description, event.id)">活动描述</el-button>
          <el-button type="primary" :icon="Edit" size="large" @click="bookEvent(event.id)">活动预约</el-button>

        </div>
        <div class="event-image">
          <img :src="event.imageUrl" alt="Event image" />
        </div>
      </div>

      <EventDetail v-if="windowOpen" @close-window="windowOpen = false" :image-src="openedImgSrc" :description="desc" :eid="eid"></EventDetail>

    </div>
    <div v-else>
      <p>没有找到符合的活动</p>
    </div>
  </div>
</template>

<style scoped>
.search-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

.search-page h1 {
  color: #333;
  margin-bottom: 20px;
  font-size: 20px;
}

.search-page input {
  width: 100%;
  padding: 12px 20px;
  margin-bottom: 40px;
  box-sizing: border-box;
  border: 2px solid #ccc;
  border-radius: 4px;
}

.events-container {
  display: flex;
  flex-direction: column;
}

.event-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f9f9f9;
  border-left: 5px solid #007bff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 5px;
  transition: all 0.3s ease;
}

.event-card h2 {
  color: #333;
}

.event-card p {
  color: #666;
}

.event-card:hover {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.event-info {
  flex: 1;
  padding-right: 20px;
  font-size: 18px;
  line-height: 2em;
}

.event-image {
  flex-basis: 40%;
}

.event-image img {
  max-width: 100%;
  height: auto;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .search-page input {
    padding: 10px;
  }

  .event-card {
    flex-direction: column;
  }

  .event-image {
    flex-basis: auto;
    margin-top: 20px;
  }
}
</style>
