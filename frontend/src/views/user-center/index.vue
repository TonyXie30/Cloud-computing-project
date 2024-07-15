<script setup lang="ts">

import {computed, onMounted, ref} from 'vue';
import {useAppStore} from "@/store/modules/app";
import axios from "axios";
import { inject } from 'vue';
import {storage} from "@/utils/storage";

const gender_options = ref([
  {
    label: "male",
    value: 'male',
  },
  {
    label: 'female',
    value: 'female'
  }
])

const subject_options = ref([
  {
    label: "数学与应用数学",
    value: "数学与应用数学"
  },
  {
    label: "物理学",
    value: "物理学"
  },
  {
    label: "化学",
    value: "化学"
  },
  {
    label: "海洋科学",
    value: "海洋科学"
  },
  {
    label: "地球物理学",
    value: "地球物理学"
  },
  {
    label: "生物科学",
    value: "生物科学"
  },
  {
    label: "生物信息学",
    value: "生物信息学"
  },
  {
    label: "统计学",
    value: "统计学"
  },
  {
    label: "理论与应用力学",
    value: "理论与应用力学"
  },
  {
    label: "机械工程",
    value: "机械工程"
  },
  {
    label: "材料科学与工程",
    value: "材料科学与工程"
  },
  {
    label: "计算机科学与技术",
    value: "计算机科学与技术"
  },
  {
    label: "智能科学与技术",
    value: "智能科学与技术"
  },
  {
    label: "数据科学与大数据技术",
    value: "数据科学与大数据技术"
  },
  {
    label: "生物医学科学",
    value: "生物医学科学"
  }

])


const globalVariable = inject('globalVariable');

let UserData = ref({
  id: 0,
  username: "user01",
  password: null,
  gender: {
    "gender": null
  },
  subject: {
    "id": null,
    "name": null
  },
  age: null,
  picture: null,
  _admin: false,
});

const isEditMode = ref(false);
const toggleEditMode = () => {
  if(isEditMode.value){
    let gender = UserData.value.gender.gender || 'male';
    let subject = UserData.value.subject.name;
    let age = UserData.value.age || 0;
    const apiUrl = 'http://'+ 'a2a127a20ddfa4b7e9442a040151db72-615736446.us-east-1.elb.amazonaws.com:8443' + '/api/profile/updateProfile?username='+storage.get('user')?.name +'&age='+age+'&subject='+ subject +'&gender='+gender
      +'&picture='+UserData.value.picture;
    console.log(apiUrl)
    const token = window.localStorage.getItem('jwt');
    axios
      .post(apiUrl, {}, {
        headers: {
          'Authorization': 'Bearer ' + token
        }
      })
      .then(response => {
        if(response.data.code===200){
          console.log("1")
        }else {
          console.log("2")
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  isEditMode.value = !isEditMode.value;
};

onMounted(() => {
  const apiUrl = 'http://'+ 'a2a127a20ddfa4b7e9442a040151db72-615736446.us-east-1.elb.amazonaws.com:8443' + '/api/profile/getProfile?username='+storage.get('user')?.name;
  console.log(apiUrl);
  const token = window.localStorage.getItem('jwt');
  axios
    .post(apiUrl, {}, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    })
    .then(response => {
      if(response.data.code===200){
        const data = response.data.data;
        UserData.value.id = data.id !== undefined ? data.id : UserData.value.id;
        UserData.value.username = data.username !== undefined ? data.username : UserData.value.username;
        UserData.value.password = data.password !== undefined ? data.password : UserData.value.password;
        UserData.value.gender.gender = data.gender && data.gender.gender !== undefined ? data.gender.gender : UserData.value.gender.gender;
        UserData.value.subject.id = data.subject && data.subject.id !== undefined ? data.subject.id : UserData.value.subject.id;
        UserData.value.subject.name = data.subject && data.subject.name !== undefined ? data.subject.name : UserData.value.subject.name;
        UserData.value.age = data.age !== undefined ? data.age : UserData.value.age;
        UserData.value.picture = data.picture !== undefined && data.picture !== 'null' ? data.picture : UserData.value.picture;
        UserData.value._admin = data._admin !== undefined ? data._admin : UserData.value._admin;
        console.log(UserData.value);
        console.log(UserData.value);
      }else {

      }
    })
    .catch(error => {
      console.error(error);
    });
})
const appStore = useAppStore();
const gap = computed(() => (appStore.isMobile ? 0 : 16));


</script>

<template>
  <NSpace vertical :size="100">

    <NGrid :x-gap="gap" :y-gap="16" responsive="screen" item-responsive>
      <NGi span="24 s:16 m:12">
        <n-card title="个人信息" size="small" class="card-wrapper">

          <template v-if="isEditMode">

            <n-input v-model:value="UserData.picture" type="text" placeholder="拖入图床链接"
                     style="width: 96px; height: 96px; border-radius: 50%; line-height: 96px; font-size: 12px"/>
          </template>
          <template v-else>
            <template v-if="!UserData.picture">
              <SoybeanAvatar class="size-96px!" />
            </template>
            <template v-else>
              <n-avatar
                round
                :size="96"
                :src="UserData.picture"
              />
            </template>
          </template>

          <br>
          <n-descriptions label-placement="left" :column="4">
            <n-descriptions-item label="名称" :span="2">
              <div class="description-content">
                <template v-if="isEditMode">
                  <n-input v-model:value="UserData.username" type="text" placeholder="请填写" disabled/>
                </template>
                <template v-else>
                  <p>{{ UserData.username }}</p>
                </template>
              </div>
            </n-descriptions-item>
            <n-descriptions-item label="性别" :span="2">
              <div class="description-content">
                <template v-if="isEditMode">
                  <n-select v-model:value="UserData.gender.gender" style="width: 180px;" placeholder="请选择" :options="gender_options"/>
                </template>
                <template v-else>
                  <p>{{ UserData.gender.gender || '未知' }}</p>
                </template>
              </div>
            </n-descriptions-item>
            <n-descriptions-item label="学科" :span="2">
              <div class="description-content">
                <template v-if="isEditMode">
                  <n-select v-model:value="UserData.subject.name" style="width: 180px;" placeholder="请选择" :options="subject_options"/>
                </template>
                <template v-else>
                  <p>{{ UserData.subject.name || '未知' }}</p>
                </template>
              </div>
            </n-descriptions-item>
            <n-descriptions-item label="年龄" :span="2">
              <div class="description-content">
                <template v-if="isEditMode">
                  <n-input v-model:value="UserData.age" type="text" placeholder="请填写" />
                </template>
                <template v-else>
                  <p>{{ UserData.age || '未知' }}</p>
                </template>
              </div>
            </n-descriptions-item>
          </n-descriptions>
          <br>
          <n-button strong secondary round type="info" @click="toggleEditMode">
            <template v-if="isEditMode">
              确认更新
            </template>
            <template v-else>
              更新信息
            </template>
          </n-button>
        </n-card>
      </NGi>
      <NGi span="24 s:24 m:10">
        <!--        <CreativityBanner />-->
      </NGi>
    </NGrid>
  </NSpace>
</template>

<style scoped>
.description-content {

  display: flex;

}
</style>
