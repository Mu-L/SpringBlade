/**
 * Copyright (c) 2018-2099, Chill Zhuang 庄骞 (bladejava@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.system.user.cache;

import org.springblade.core.cache.constant.CacheConstant;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.entity.UserInfo;
import org.springblade.system.user.feign.IUserClient;

/**
 * 用户缓存工具类
 * <p>
 * 通过 {@link SpringUtil} 在运行时懒加载 Feign 客户端，供各服务复用用户数据缓存。
 * 用户数据缓存在 {@link CacheConstant#USER_CACHE} 域中，用户信息变更时由用户服务清理该缓存域。
 *
 * @author Chill
 */
public class UserCache {

	private static final String USER_ID = "user:id:";

	private static IUserClient userClient;

	private static IUserClient getUserClient() {
		if (userClient == null) {
			userClient = SpringUtil.getBean(IUserClient.class);
		}
		return userClient;
	}

	/**
	 * 获取用户
	 *
	 * @param userId 用户id
	 * @return User
	 */
	public static User getUser(Long userId) {
		return CacheUtil.get(CacheConstant.USER_CACHE, USER_ID, userId, () -> {
			UserInfo userInfo = getUserClient().userInfo(userId).getData();
			return userInfo == null ? null : userInfo.getUser();
		});
	}

}
