import { useTitle } from '@vueuse/core';
import type { Router } from 'vue-router';

export function createDocumentTitleGuard(router: Router) {
  router.afterEach(to => {
    useTitle(to.meta.title);
  });
}
