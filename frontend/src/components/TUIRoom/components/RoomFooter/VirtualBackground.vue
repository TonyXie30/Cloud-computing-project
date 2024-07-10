<template>
  <div v-if="componentConfig.visible" class="virtualBackground-control-container">
    <icon-button :title="t('VirtualBackground')" @click-icon="openSettingPanel">
      <virtual-background-icon></virtual-background-icon>
    </icon-button>

    <Dialog
      v-model="isDialogVisible" :title="t('VirtualBackground')" width="600px" :modal="true"
      :append-to-room-container="true" @close="closeSettingPanel"
    >
      <div id="stream-preview" class="stream-preview"></div>
      <div class="setting">
        <div
          :class="['setting-item', currentVirtualBackground === 'close' ? 'active' : '']"
          @click="closeVirtualBackground"
        >
          <i class="setting-item-icon">
            <img :src="CloseVirtualBackground" alt="close" style="width: 32px;" />
          </i>
          <span>{{ t('Close') }}</span>
        </div>
        <div
          :class="['setting-item', currentVirtualBackground === 'blur' ? 'active' : '']"
          @click="openBlurredBackground"
        >
          <i class="setting-item-icon">
            <img :src="BlurredBackground" alt="blurred" />
          </i>
          <span>{{ t('BlurredBackground') }}</span>
        </div>
      </div>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { nextTick, ref } from 'vue';
import IconButton from '../common/base/IconButton.vue';
import VirtualBackgroundIcon from '../common/icons/VirtualBackgroundIcon.vue';
import { useI18n } from '../../locales';
import { roomService } from '../../services';
import Dialog from '../common/base/Dialog';
import CloseVirtualBackground from '../../assets/imgs/close-virtual-background.png';
import BlurredBackground from '../../assets/imgs/blurred-background.png';

const { t } = useI18n();
const componentConfig = roomService.componentManager.getComponentConfig('VirtualBackground');

const currentVirtualBackground = ref<'close' | 'blur'>('close');
const isDialogVisible = ref(false);
const openSettingPanel = async () => {
  roomService.virtualBackground.initVirtualBackground();
  isDialogVisible.value = true;
  await nextTick();
  roomService.roomEngine.instance?.startCameraDeviceTest({ view: 'stream-preview' });
};
const closeSettingPanel = () => {
  isDialogVisible.value = false;
  roomService.roomEngine.instance?.stopCameraDeviceTest();
};

const openBlurredBackground = () => {
  currentVirtualBackground.value = 'blur';
  roomService.virtualBackground.toggleVirtualBackground(true);
};
const closeVirtualBackground = () => {
  currentVirtualBackground.value = 'close';
  roomService.virtualBackground.toggleVirtualBackground(false);
};
</script>

<style lang="scss" scoped>
.stream-preview {
  display: flex;
  box-sizing: border-box;
  border-radius: 8px;
  overflow: hidden;
  min-height: 310px;
  background-color: #000;
}

.setting {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 10px;
  padding: 1rem;
  border: 1px solid #E4E8EE;
  border-radius: 8px;

  &-item {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    border-radius: 8px;
    color: #4F586B;
    font-size: 12px;
    border: 1px solid transparent;

    &-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f0f3fa;
      border-radius: 8px;
      width: 54px;
      height: 54px;
      overflow: hidden;
    }
  }

  &-item.active {
    background-color: #1C66E5;
    border: 1px solid #1C66E5;
    color: #fff;
  }
}
</style>
