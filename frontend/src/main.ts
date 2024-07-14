import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css';
import './plugins/assets';
import { setupDayjs, setupIconifyOffline, setupLoading, setupNProgress } from './plugins';
import { setupStore } from './store';
import { setupRouter } from './router';
import { setupI18n } from './locales';
import App from './App.vue';
// gg

async function setupApp() {
  setupLoading();

  setupNProgress();

  setupIconifyOffline();

  setupDayjs();

  const app = createApp(App);

  setupStore(app);

  await setupRouter(app);

  setupI18n(app);


  const globalVariable = {
    someValue: 'user01'
  }

  app.provide('globalVariable', globalVariable)


  app.use(ElementPlus);
  app.use(Antd);
  app.mount('#app');
}

setupApp();
