package com.idlix;
public final class SearchApiResponse {
    private final java.util.List results;
    private final long total;

    public SearchApiResponse(java.util.List p1, long p2)
    {
        this.results = p1;
        this.total = p2;
        return;
    }

    public static synthetic com.idlix.SearchApiResponse copy$default(com.idlix.SearchApiResponse p0, java.util.List p1, long p2, int p4, Object p5)
    {
        if ((p4 & 1) != 0) {
            p1 = p0.results;
        }
        if ((p4 & 2) != 0) {
            p2 = p0.total;
        }
        return p0.copy(p1, p2);
    }

    public final java.util.List component1()
    {
        return this.results;
    }

    public final long component2()
    {
        return this.total;
    }

    public final com.idlix.SearchApiResponse copy(java.util.List p2, long p3)
    {
        return new com.idlix.SearchApiResponse(p2, p3);
    }

    public boolean equals(Object p8)
    {
        if (this != p8) {
            if ((p8 instanceof com.idlix.SearchApiResponse)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.results, ((com.idlix.SearchApiResponse) p8).results)) {
                    if (this.total == ((com.idlix.SearchApiResponse) p8).total) {
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
            return 1;
        }
    }

    public final java.util.List getResults()
    {
        return this.results;
    }

    public final long getTotal()
    {
        return this.total;
    }

    public int hashCode()
    {
        return ((this.results.hashCode() * 31) + Long.hashCode(this.total));
    }

    public String toString()
    {
        return new StringBuilder().append("SearchApiResponse(results=").append(this.results).append(", total=").append(this.total).append(41).toString();
    }
}
