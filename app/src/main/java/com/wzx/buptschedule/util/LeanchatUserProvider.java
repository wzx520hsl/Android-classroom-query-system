package com.wzx.buptschedule.util;

import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.wzx.buptschedule.bean.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wli on 15/12/4.
 */
public class LeanchatUserProvider implements ThirdPartUserUtils.ThirdPartDataProvider {
  @Override
  public ThirdPartUserUtils.ThirdPartUser getSelf() {
    return new ThirdPartUserUtils.ThirdPartUser("daweibayu", "daweibayu",
      "http://ac-x3o016bx.clouddn.com/CsaX0GuXL7gXWBkaBFXfBWZPlcanClEESzHxSq2T.jpg");
  }

  @Override
  public void getFriend(String userId, final ThirdPartUserUtils.FetchUserCallBack callBack) {
    if (UserCacheUtils.hasCachedUser(userId)) {
      callBack.done(Arrays.asList(getThirdPartUser(UserCacheUtils.getCachedUser(userId))), null);
    } else {
      UserCacheUtils.fetchUsers(Arrays.asList(userId), new UserCacheUtils.CacheUserCallback() {
        @Override
        public void done(List<UserInfo> userList, Exception e) {
          callBack.done(getThirdPartUsers(userList), e);
        }
      });
    }
  }

  @Override
  public void getFriends(List<String> list, final ThirdPartUserUtils.FetchUserCallBack callBack) {
    UserCacheUtils.fetchUsers(list, new UserCacheUtils.CacheUserCallback() {
      @Override
      public void done(List<UserInfo> userList, Exception e) {
        callBack.done(getThirdPartUsers(userList), e);
      }
    });
  }

  @Override
  public void getFriends(int skip, int limit, final ThirdPartUserUtils.FetchUserCallBack callBack) {
   /* FriendsManager.fetchFriends(false, new FindCallback<UserInfo>() {
      @Override
      public void done(List<UserInfo> list, AVException e) {
        callBack.done(getThirdPartUsers(list), e);
      }
    });*/
  }

  private static ThirdPartUserUtils.ThirdPartUser getThirdPartUser(UserInfo userInfo) {
    return new ThirdPartUserUtils.ThirdPartUser(userInfo.getObjectId(), userInfo.getUserName(),
            userInfo.getAvatarUrl());
  }

  public static List<ThirdPartUserUtils.ThirdPartUser> getThirdPartUsers(List<UserInfo> userInfos) {
    List<ThirdPartUserUtils.ThirdPartUser> thirdPartUsers = new ArrayList<>();
    for (UserInfo user : userInfos) {
      thirdPartUsers.add(getThirdPartUser(user));
    }
    return thirdPartUsers;
  }
}
