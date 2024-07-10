<script setup lang="ts">
import { computed } from 'vue';
import { useAppStore } from '@/store/modules/app';
import HeaderBanner from './modules/header-banner.vue';
import ProjectNews from './modules/project-news.vue';
import CreativityBanner from './modules/creativity-banner.vue';
// import { ref } from 'vue'

const appStore = useAppStore();

const gap = computed(() => (appStore.isMobile ? 0 : 16));

// const src = ref('bgvideo.mp4')
</script>

<template>
  <NSpace vertical :size="100">
    <HeaderBanner />
    <NGrid :x-gap="gap" :y-gap="16" responsive="screen" item-responsive>
      <NGi span="24 s:24 m:14">
        <ProjectNews />
      </NGi>
      <NGi span="24 s:24 m:10">
        <CreativityBanner />
      </NGi>
    </NGrid>
  </NSpace>
</template>

<style scoped></style>
