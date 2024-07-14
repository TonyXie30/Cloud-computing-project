<script setup lang="ts">
import { computed, reactive } from 'vue';
import { $t } from '@/locales';
import { useRouterPush } from '@/hooks/common/router';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { useCaptcha } from '@/hooks/business/captcha';
import axios from "axios";

defineOptions({
  name: 'CodeLogin'
});

const { toggleLoginModule } = useRouterPush();
const { formRef, validate } = useNaiveForm();
const { label, isCounting, loading, getCaptcha } = useCaptcha();

interface FormModel {
  phone: string;
  code: string;
  password: string;
  confirmPassword: string;
}

const model: FormModel = reactive({
  phone: '',
  code: '',
  password: '',
  confirmPassword: ''
});

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  const { formRules, createConfirmPwdRule } = useFormRules();

  return {
    phone: formRules.userName,
    code: formRules.code,
    password: formRules.pwd,
    confirmPassword: createConfirmPwdRule(model.password)
  };
});

async function handleSubmit() {
  const apiUrl = 'http://localhost:8443/api/verify/register';
  const logindata = {
    username: model.phone,
    password: model.password
  };

  await axios
    .post(apiUrl, logindata)
    .then(response => {
      console.log(response.data);
      if(response.data.code===200){
        window.$message?.success('注册成功');
      }else {
        window.$message?.error('注册失败');
      }
    })
    .catch(error => {
      console.error(error);
      window.$message?.error('注册失败');
    });

  // await validate();
  // // request to register
  // window.$message?.success($t('page.login.common.validateSuccess'));
}
</script>

<template>
  <NForm ref="formRef" :model="model" :rules="rules" size="large" :show-label="false">
    <NFormItem path="phone">
      <NInput v-model:value="model.phone" :placeholder="$t('page.login.common.phonePlaceholder')" />

    </NFormItem>
<!--    <NFormItem path="code">-->
<!--      <div class="w-full flex-y-center gap-16px">-->
<!--        <NInput v-model:value="model.code" :placeholder="$t('page.login.common.codePlaceholder')" />-->
<!--        <NButton size="large" :disabled="isCounting" :loading="loading" @click="getCaptcha(model.phone)">-->
<!--          {{ label }}-->
<!--        </NButton>-->
<!--      </div>-->
<!--    </NFormItem>-->
    <NFormItem path="password">
      <NInput
        v-model:value="model.password"
        type="password"
        show-password-on="click"
        :placeholder="$t('page.login.common.passwordPlaceholder')"
      />
    </NFormItem>
    <NFormItem path="confirmPassword">
      <NInput
        v-model:value="model.confirmPassword"
        type="password"
        show-password-on="click"
        :placeholder="$t('page.login.common.confirmPasswordPlaceholder')"
      />
    </NFormItem>
    <NSpace vertical :size="18" class="w-full">
      <NButton type="primary" size="large" round block @click="handleSubmit">
        {{ $t('common.confirm') }}
      </NButton>
      <NButton size="large" round block @click="toggleLoginModule('pwd-login')">
        {{ $t('page.login.common.back') }}
      </NButton>
    </NSpace>
  </NForm>
</template>

<style scoped></style>
