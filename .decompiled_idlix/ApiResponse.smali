package com.idlix;
public final class ApiResponse {
    private final java.util.List data;
    private final com.idlix.Meta meta;
    private final com.idlix.Pagination pagination;

    public ApiResponse()
    {
        this(0, 0, 0, 7, 0);
        return;
    }

    public ApiResponse(java.util.List p1, com.idlix.Pagination p2, com.idlix.Meta p3)
    {
        this.data = p1;
        this.pagination = p2;
        this.meta = p3;
        return;
    }

    public synthetic ApiResponse(java.util.List p2, com.idlix.Pagination p3, com.idlix.Meta p4, int p5, kotlin.jvm.internal.DefaultConstructorMarker p6)
    {
        if ((p5 & 1) != 0) {
            p2 = kotlin.collections.CollectionsKt.emptyList();
        }
        if ((p5 & 2) != 0) {
            p3 = 0;
        }
        if ((p5 & 4) != 0) {
            p4 = 0;
        }
        this(p2, p3, p4);
        return;
    }

    public static synthetic com.idlix.ApiResponse copy$default(com.idlix.ApiResponse p0, java.util.List p1, com.idlix.Pagination p2, com.idlix.Meta p3, int p4, Object p5)
    {
        if ((p4 & 1) != 0) {
            p1 = p0.data;
        }
        if ((p4 & 2) != 0) {
            p2 = p0.pagination;
        }
        if ((p4 & 4) != 0) {
            p3 = p0.meta;
        }
        return p0.copy(p1, p2, p3);
    }

    public final java.util.List component1()
    {
        return this.data;
    }

    public final com.idlix.Pagination component2()
    {
        return this.pagination;
    }

    public final com.idlix.Meta component3()
    {
        return this.meta;
    }

    public final com.idlix.ApiResponse copy(java.util.List p2, com.idlix.Pagination p3, com.idlix.Meta p4)
    {
        return new com.idlix.ApiResponse(p2, p3, p4);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.ApiResponse)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.data, ((com.idlix.ApiResponse) p6).data)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.pagination, ((com.idlix.ApiResponse) p6).pagination)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.meta, ((com.idlix.ApiResponse) p6).meta)) {
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
            return 1;
        }
    }

    public final java.util.List getData()
    {
        return this.data;
    }

    public final com.idlix.Meta getMeta()
    {
        return this.meta;
    }

    public final com.idlix.Pagination getPagination()
    {
        return this.pagination;
    }

    public int hashCode()
    {
        com.idlix.Meta v2_0;
        int v1_1 = (this.data.hashCode() * 31);
        int v3 = 0;
        if (this.pagination != null) {
            v2_0 = this.pagination.hashCode();
        } else {
            v2_0 = 0;
        }
        int v0_1 = ((v1_1 + v2_0) * 31);
        if (this.meta != null) {
            v3 = this.meta.hashCode();
        }
        return (v0_1 + v3);
    }

    public String toString()
    {
        return new StringBuilder().append("ApiResponse(data=").append(this.data).append(", pagination=").append(this.pagination).append(", meta=").append(this.meta).append(41).toString();
    }
}
