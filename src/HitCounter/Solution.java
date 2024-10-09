package HitCounter;

import java.util.concurrent.atomic.AtomicInteger;

public class Solution implements Q06WebpageVisitCounterInterface{
    private Helper06 helper;
    private AtomicInteger[] visitCounts;
    private int totalPages;

    public Solution(){}

    @Override
    public void init(int totalPages, Helper06 helper) {
        this.helper = helper;
        this.totalPages = totalPages;
        visitCounts = new AtomicInteger[totalPages];
        for (int i = 0; i < totalPages; i++) {
            visitCounts[i] = new AtomicInteger(0);
        }
        helper.println("网页访问计数器初始化完成，总页面数: " + totalPages);
    }

    @Override
    public void incrementVisitCount(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= totalPages) {
            helper.println("无效的页面索引: " + pageIndex);
            return;
        }

        int newCount = visitCounts[pageIndex].incrementAndGet();
        helper.println("页面 " + pageIndex + " 的访问计数增加到: " + newCount);
    }

    @Override
    public int getVisitCount(int pageIndex) {
        if(pageIndex < 0 || pageIndex >= totalPages) {
            helper.println("无效的页面索引: " + pageIndex);
            return -1; // 返回 -1 表示错误
        }
        int count = visitCounts[pageIndex].get();
        helper.println("页面 " + pageIndex + " 的总访问计数为: " + count);
        return count;
    }
}
