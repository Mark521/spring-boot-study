                                        ** git命令流程**







 1.git status 查看分支状态,存在文件，则执行第二步
 2,使用commit保存代码：


   # git add 路径名 //添加新创建的文件到git
   # git commit
   # git commit -a --amend //保存到上一个历史版本，建议
   # git commit -a
   # 所有未被跟踪的修改加到commit里
  #  3,回到master 执行 git pull --rebase
  #  4,回到分支 arc feature <分知名>
  #  5,执行git rebase 将master的更新同步到当前分支，并判断是否冲突，有冲突合并冲突
  #  6,冲突处理好后，执行arc diff 提交代码审核
  #  7,代码审核通过后，执行arc land提交代码到本机master和服务端master

在master执行更新同步命令：先执行git fetch ,在执行git rebase命令

二 切换到一个分支上打包
# git checkout -b releases_VegaAndroid_vegaandroid-20151116-rAc988fca3062e c988fca3062e //切换到一个固定版本打包
# git push 按照说明最后一个命令来

# git cherry-pick 77a7182423d9 //增加一个diff到这个分支上来


> 如果有冲突、解决冲突， git add，git cherry-pick --continue

# git push //提交代码到这个分支（注不需要arc diff和arc land流程）

下面的命令如有异议欢迎进行更新修改
显示有变更的文件
git status

显示当前分支的版本历史
git log

切换到主分支上更新代码再新建/切换分支
arc feature master
git fetch (下载远程仓库的所有变动)
git rebase
arc feature 5-1
git rebase
更新主代码也可以用以下方式：
git fetch origin
git rebase origin/master

添加所有文件
git add -A
或者 git add .

列出所有本地分支
git branch

删除分支（需要在主分支上进行操作）
git branch -D 5-1

提交代码
git commit -a -m aaa
git status 查看状态
arc diff 编辑信息
arc feature 查看生成的diff编号
arc land 提交

在之前已经有本地提交过，git status之后发现本地有删除和添加文件修改时：
git add . / git add -A
git commit -a --amend (使用一次新的commit，替代上一次提交)
:q + esc

当有两个提交记录的时候，可以将两次提交合并一起提交：
git rebase -i xxx(xxx为要合并的ID)
squash 合并

添加再追加到上个提交的版本（或上个生成的diff编号）
git add .
git rebase --continue

rebase后出现冲突
git patch Dxxx(xxx为提交的分支状态)

如果不小心在master分支上修改文件，并且提交执行过arc diff操作，此时再arc land会提交失败。可以进行如下操作进行修复：
git reset --hard HEAD~1
git rebase
git fetch
git status
arc patch D196 (196为这次操作arc diff产生的序号)
arc lang (= arc land)

恢复暂存区的指定文件到工作区
git checkout -- [file]

重置暂存区的指定文件，与上一次commit保持一致，但工作区不变
git reset [file]

重置暂存区与工作区，与上一次commit保持一致
git reset --hard

重置当前分支的指针为指定commit，同时重置暂存区，但工作区不变
git reset [commit]


   如果一次代码太多，无法提交的解决方案
   1 回到master git fetch && git rebase 
   2 git merge 分支名
   3 git push

回到以前提交代码的分支
git checkout 3385ed92b6e4 -b 12-1_fixbug