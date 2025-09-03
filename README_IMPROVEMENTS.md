# 首页和底部导航栏改进说明

## 主要改进内容

### 1. 首页推荐/关注切换优化

#### 横条指示器状态变化
- 完善了推荐和关注切换时的文本下横条状态变化
- 添加了动态的横条指示器，显示当前选中的页面
- 指示器使用橙色背景，高度为3dp，圆角设计
- 切换时指示器会平滑地在推荐和关注之间移动

#### 文本状态变化
- 选中状态：黑色文字，20sp大小，粗体显示
- 未选中状态：灰色文字，20sp大小，正常字重
- 切换时文字状态会同步更新

### 2. 底部导航栏重构

#### 五个主页面管理
- 首页：包含推荐和关注两个子页面的切换
- 视频页面：独立的视频功能页面
- 发现页面：独立的发现功能页面
- 消息页面：独立的消息功能页面
- 个人中心：独立的个人中心页面

#### 页面切换机制
- 使用Fragment管理器来管理不同页面的显示
- 首页使用ViewPager2来管理推荐和关注的子页面
- 其他页面使用独立的Fragment来显示
- 切换时ViewPager和Fragment容器会正确显示/隐藏

#### 导航状态管理
- 底部导航图标会根据选中状态改变颜色
- 选中状态：黑色
- 未选中状态：灰色
- 支持返回键处理，防止意外退出应用

### 3. 技术实现细节

#### Fragment管理
- 创建了VideoFragment、MessageFragment、MineFragment等新Fragment
- 使用supportFragmentManager来管理Fragment的生命周期
- 正确清理不需要的Fragment，避免内存泄漏

#### 布局优化
- 使用FrameLayout来管理ViewPager和Fragment容器
- 确保两个容器不会同时占用空间
- 响应式布局，适配不同屏幕尺寸

#### 状态同步
- 顶部导航状态与ViewPager页面状态同步
- 底部导航状态与当前显示页面同步
- 统一的UI状态管理

## 使用方法

1. 启动应用后默认显示首页（推荐页面）
2. 点击顶部的"推荐"或"关注"可以切换子页面
3. 点击底部的导航图标可以切换到不同的主页面
4. 在非首页时，按返回键会回到首页

## 文件结构

```
app/src/main/java/com/example/weiboxx/ui/
├── MainActivity.kt                    # 主活动，管理所有页面切换
├── home/
│   ├── PostListFragment.kt           # 推荐页面
│   └── FollowFragment.kt             # 关注页面
├── video/
│   └── VideoFragment.kt              # 视频页面
├── discover/
│   └── DiscoverFragment.kt           # 发现页面
├── message/
│   └── MessageFragment.kt            # 消息页面
└── mine/
    └── MineFragment.kt               # 个人中心页面
```

```
app/src/main/res/layout/
├── activity_main.xml                  # 主活动布局
├── fragment_video.xml                 # 视频页面布局
├── fragment_message.xml               # 消息页面布局
└── fragment_mine.xml                  # 个人中心页面布局
```

```
app/src/main/res/drawable/
├── indicator_chosen.xml               # 选中状态指示器
└── indicator_unselected.xml           # 未选中状态指示器
```

## 注意事项

1. 确保所有Fragment类都已正确创建
2. 布局文件中的ID要与代码中的引用一致
3. 在切换页面时注意Fragment的生命周期管理
4. 测试不同屏幕尺寸下的显示效果

