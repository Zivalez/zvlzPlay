package com.idlix;
public final class Pagination {
    private final Integer limit;
    private final Integer page;
    private final Integer total;
    private final Integer totalPages;

    public Pagination()
    {
        this(0, 0, 0, 0, 15, 0);
        return;
    }

    public Pagination(Integer p1, Integer p2, Integer p3, Integer p4)
    {
        this.page = p1;
        this.limit = p2;
        this.total = p3;
        this.totalPages = p4;
        return;
    }

    public synthetic Pagination(Integer p2, Integer p3, Integer p4, Integer p5, int p6, kotlin.jvm.internal.DefaultConstructorMarker p7)
    {
        if ((p6 & 1) != 0) {
            p2 = 0;
        }
        if ((p6 & 2) != 0) {
            p3 = 0;
        }
        if ((p6 & 4) != 0) {
            p4 = 0;
        }
        if ((p6 & 8) != 0) {
            p5 = 0;
        }
        this(p2, p3, p4, p5);
        return;
    }

    public static synthetic com.idlix.Pagination copy$default(com.idlix.Pagination p0, Integer p1, Integer p2, Integer p3, Integer p4, int p5, Object p6)
    {
        if ((p5 & 1) != 0) {
            p1 = p0.page;
        }
        if ((p5 & 2) != 0) {
            p2 = p0.limit;
        }
        if ((p5 & 4) != 0) {
            p3 = p0.total;
        }
        if ((p5 & 8) != 0) {
            p4 = p0.totalPages;
        }
        return p0.copy(p1, p2, p3, p4);
    }

    public final Integer component1()
    {
        return this.page;
    }

    public final Integer component2()
    {
        return this.limit;
    }

    public final Integer component3()
    {
        return this.total;
    }

    public final Integer component4()
    {
        return this.totalPages;
    }

    public final com.idlix.Pagination copy(Integer p2, Integer p3, Integer p4, Integer p5)
    {
        return new com.idlix.Pagination(p2, p3, p4, p5);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Pagination)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.page, ((com.idlix.Pagination) p6).page)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.limit, ((com.idlix.Pagination) p6).limit)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.total, ((com.idlix.Pagination) p6).total)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.totalPages, ((com.idlix.Pagination) p6).totalPages)) {
                                return 1;
                            } else {
                                return 0;
                            }
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public final Integer getLimit()
    {
        return this.limit;
    }

    public final Integer getPage()
    {
        return this.page;
    }

    public final Integer getTotal()
    {
        return this.total;
    }

    public final Integer getTotalPages()
    {
        return this.totalPages;
    }

    public int hashCode()
    {
        int v0_4;
        int v1_0 = 0;
        if (this.page != null) {
            v0_4 = this.page.hashCode();
        } else {
            v0_4 = 0;
        }
        Integer v3_1;
        int v2_3 = (v0_4 * 31);
        if (this.limit != null) {
            v3_1 = this.limit.hashCode();
        } else {
            v3_1 = 0;
        }
        Integer v3_4;
        int v0_1 = ((v2_3 + v3_1) * 31);
        if (this.total != null) {
            v3_4 = this.total.hashCode();
        } else {
            v3_4 = 0;
        }
        int v2_1 = ((v0_1 + v3_4) * 31);
        if (this.totalPages != null) {
            v1_0 = this.totalPages.hashCode();
        }
        return (v2_1 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Pagination(page=").append(this.page).append(", limit=").append(this.limit).append(", total=").append(this.total).append(", totalPages=").append(this.totalPages).append(41).toString();
    }
}
