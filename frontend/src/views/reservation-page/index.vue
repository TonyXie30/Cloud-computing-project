<script setup lang="ts">
import { ref } from 'vue'
import type { CalendarDateType, CalendarInstance } from 'element-plus'

import {storage} from "@/utils/storage";
import axios from "axios";
import {onMounted} from "vue";
import {useMessage} from "naive-ui";

const calendar = ref<CalendarInstance>()
const selectDate = (val: CalendarDateType) => {
  if (!calendar.value) return
  calendar.value.selectDate(val)
}

interface Event {
  id: number;
  name: string;
  location: string;
  type: string;
  date: string;
  description: string;
  imageUrl: string;
}

interface EventBrief{
  id: number;
  name: string;
  date: string;
  location: string;
}
const message = useMessage()
const eventsBrief = ref<EventBrief[]>([]);

const events = ref<Event[]>([]);

const fetchEvents = async () => {
  const apiUrl = 'http://'+ 'a2a127a20ddfa4b7e9442a040151db72-615736446.us-east-1.elb.amazonaws.com:8443' + '/api/main/getSelfEvents?username=' + storage.get('user')?.name
  const token = window.localStorage.getItem('jwt')
  try {
    const response = await axios.post(apiUrl,{}, {
      headers:{
        'Authorization': `Bearer ${token}`
      }
    })
    if (response.data.code === 200) {
      events.value = response.data.data.map((event: any) => ({
        id: event.id,
        name: event.name,
        location: event.place.name,
        type: '公开活动', // 你可以根据具体情况设置类型
        date: new Date(event.startTime).toLocaleDateString(), // 仅获取日期部分
        description: event.description,
        imageUrl: event.picture
      }))
      console.log(events.value);
      // console.log(calendar.range());
    } else {
      console.error('Error fetching events:', response.data.message)
    }
  } catch (error) {
    console.error('Error fetching events:', error)
  }
}

onMounted(() => {
  fetchEvents()
})

function bookCancel(id: number) {
  const apiUrl = 'http://'+ 'a2a127a20ddfa4b7e9442a040151db72-615736446.us-east-1.elb.amazonaws.com:8443' + '/api/main/leaveEvent?username='+storage.get('user')?.name+'&eventId='+id;
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

        message.success("取消预约成功！")
      }else {
        message.warning("取消预约成功！")
      }
    })
    .catch(error => {
      console.error(error);
    });
  events.value = events.value.filter((event) => event.id !== id);
  eventsBrief.value = eventsBrief.value.filter((event) => event.id !== id);
}

// 检查给定日期是否有事件
const hasEvent = (date: String): string => {
  let dateParts = date.split('-');
  // console.log(dateParts); // 调试输出，查看dateParts的内容
  let year = dateParts[0];
  let month = dateParts[1] ? dateParts[1].replace(/^0+/, '') : ''; // 去掉月份前的0
  let day = dateParts[2] ? dateParts[2].replace(/^0+/, '') : ''; // 去掉日期前的0

  let formattedDateString = `${year}/${month}/${day}`;
  // console.log(formattedDateString); // 调试输出，查看格式化后的日期字符串
  if(events.value.some(event => event.date === formattedDateString)){
    // console.log(formattedDateString)
    return '✔️'
  }else{
    return ''
  }
}

const showEvent = (date: String, jud :boolean): void =>{
  if(jud){
    // console.log(date);
    let dateParts = date.split('-');

    let year = dateParts[0];
    let month = dateParts[1] ? dateParts[1].replace(/^0+/, '') : ''; // 去掉月份前的0
    let day = dateParts[2] ? dateParts[2].replace(/^0+/, '') : ''; // 去掉日期前的0

    let formattedDateString = `${year}/${month}/${day}`;
    if(events.value.some(event => {
      if(event.date === formattedDateString) {
        if (eventsBrief.value.length === 0) {
          eventsBrief.value.push({
            id: event.id,
            name: event.name,
            date: event.date,
            location: event.location
          });
        } else {
          eventsBrief.value[0] = {
            id: event.id,
            name: event.name,
            date: event.date,
            location: event.location
          };
        }
      }
      return event.date === formattedDateString
    })) {console.log(eventsBrief.value)}

  }
}


</script>

<template>
  <div>
    <div class="upper">

      <el-calendar ref="calendar">
        <template #header="{ date }">
          <span>校园活动预约表</span>
          <span>{{ date }}</span>
          <el-button-group>
            <el-button size="small" @click="selectDate('prev-year')">
              上一年
            </el-button>
            <el-button size="small" @click="selectDate('prev-month')">
              上月
            </el-button>
            <el-button size="small" @click="selectDate('today')">Today</el-button>
            <el-button size="small" @click="selectDate('next-month')">
              下月
            </el-button>
            <el-button size="small" @click="selectDate('next-year')">
              下一年
            </el-button>
          </el-button-group>
        </template>
        <template #date-cell="{ data }">
          <p :class="data.isSelected ? 'is-selected' : ''">
            {{ data.day.split('-').slice(2).join('-') }}
<!--            {{ data.isSelected ? '✔️' : '' }}-->
            {{ hasEvent(data.day)}}
            {{ showEvent(data.day, data.isSelected)}}
          </p>
        </template>
      </el-calendar>
    </div>
    <div class="lower">
      <el-card v-for="event in eventsBrief" :key="event.id" class="event-card">
        <template #header>
          <div class="card-header">
            <span>活动名称： {{ event.name }}</span>
          </div>
        </template>
        <div class="info">
          <div class="date">活动日期： {{ event.date }}</div>
          <div>活动地点： {{ event.location }}</div>
        </div>
        <div class="cancel-button">
          <el-button type="danger" round @click="bookCancel(event.id)">取消预约</el-button>
        </div>
      </el-card>
    </div>

  </div>
</template>

<style scoped>
.lower {
  display: flex;
  justify-content: center;
}

.event-card {
  margin-top: 40px;
  width: 25%;
}

.info {
  margin-bottom: 10px;
}

.date {
  margin-bottom: 10px;
}

.cancel-button {
  display: flex;
  justify-content: center;
}


.has-event {
  position: relative;
  background-color: #e0f7fa;
}

.has-event::after {
  content: '✔️';
  position: absolute;
  right: 0;
  bottom: 0;
  font-size: 12px;
  color: green;
}
</style>

