<script setup lang="ts">
import { ref, watch } from 'vue'
import Comment from './comment.vue'
const props = defineProps<{ imageSrc: string, description: string, eid: number }>();
const desc = ref(props.description);
const src = ref(props.imageSrc);
const eventId = ref(props.eid)

// Watch for changes in props and update the refs accordingly
watch(() => props.description, (newDesc) => {
  desc.value = newDesc;
});

watch(() => props.imageSrc, (newSrc) => {
  src.value = newSrc;
});

console.log(desc.value);
console.log(src.value);
console.log(eventId.value);
</script>

<template>
  <div class='modal'>
    <div class='modal-content'>
      <span class="close" @click="$emit('close-window')">&times;</span>
      <div class='event'>
        <div class="left">
          <p>{{ desc }}</p>
        </div>
        <div class='right'>
          <img class='modal-img' :src='src' alt='The image of the event' />
        </div>
      </div>
      <Comment :eventId="eventId"></Comment>
    </div>
  </div>

</template>

<style scoped>
.modal {

  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 40px;
  border-radius: 5px;
  max-height: 80%;
  overflow: auto;
  width: 60%;
  position: relative;
  font-size: 15px;
}

.event {
  display: flex;
  justify-content: space-around;
  align-items: stretch; /* Ensure both sides have the same height */
}

.left, .right {
  flex: 1; /* Make both sides take equal space */
  display: flex;
  align-items: center;
  justify-content: center;
}

.left {
  box-shadow: 0 0 0 0.5rem skyblue;
  border-radius: 12px;
  font: bold 1rem sans-serif;
  margin: 2rem;
  padding: 1rem;
  outline-offset: 0.5rem;
  overflow: auto;
}

.modal-img {
  max-width: 100%;
  max-height: 100%; /* Ensure image fits within the right side */
  margin-top: 20px;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.close {
  position: absolute;
  top: 10px;
  right: 20px;
  font-size: 30px;
  color: #333;
  cursor: pointer;

}
</style>
