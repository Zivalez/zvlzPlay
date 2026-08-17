package com.idlix;
public final class SeasonWrapper {
    private final com.idlix.Season season;

    public SeasonWrapper()
    {
        this(0, 1, 0);
        return;
    }

    public SeasonWrapper(com.idlix.Season p1)
    {
        this.season = p1;
        return;
    }

    public synthetic SeasonWrapper(com.idlix.Season p1, int p2, kotlin.jvm.internal.DefaultConstructorMarker p3)
    {
        if ((p2 & 1) != 0) {
            p1 = 0;
        }
        this(p1);
        return;
    }

    public static synthetic com.idlix.SeasonWrapper copy$default(com.idlix.SeasonWrapper p0, com.idlix.Season p1, int p2, Object p3)
    {
        if ((p2 & 1) != 0) {
            p1 = p0.season;
        }
        return p0.copy(p1);
    }

    public final com.idlix.Season component1()
    {
        return this.season;
    }

    public final com.idlix.SeasonWrapper copy(com.idlix.Season p2)
    {
        return new com.idlix.SeasonWrapper(p2);
    }

    public boolean equals(Object p5)
    {
        if (this != p5) {
            if ((p5 instanceof com.idlix.SeasonWrapper)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.season, ((com.idlix.SeasonWrapper) p5).season)) {
                    return 1;
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

    public final com.idlix.Season getSeason()
    {
        return this.season;
    }

    public int hashCode()
    {
        int v0_2;
        if (this.season != null) {
            v0_2 = this.season.hashCode();
        } else {
            v0_2 = 0;
        }
        return v0_2;
    }

    public String toString()
    {
        return new StringBuilder().append("SeasonWrapper(season=").append(this.season).append(41).toString();
    }
}
