package com.idlix;
public final class Genre {
    private final String id;
    private final String name;
    private final String slug;

    public Genre()
    {
        this(0, 0, 0, 7, 0);
        return;
    }

    public Genre(String p1, String p2, String p3)
    {
        this.id = p1;
        this.name = p2;
        this.slug = p3;
        return;
    }

    public synthetic Genre(String p2, String p3, String p4, int p5, kotlin.jvm.internal.DefaultConstructorMarker p6)
    {
        if ((p5 & 1) != 0) {
            p2 = 0;
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

    public static synthetic com.idlix.Genre copy$default(com.idlix.Genre p0, String p1, String p2, String p3, int p4, Object p5)
    {
        if ((p4 & 1) != 0) {
            p1 = p0.id;
        }
        if ((p4 & 2) != 0) {
            p2 = p0.name;
        }
        if ((p4 & 4) != 0) {
            p3 = p0.slug;
        }
        return p0.copy(p1, p2, p3);
    }

    public final String component1()
    {
        return this.id;
    }

    public final String component2()
    {
        return this.name;
    }

    public final String component3()
    {
        return this.slug;
    }

    public final com.idlix.Genre copy(String p2, String p3, String p4)
    {
        return new com.idlix.Genre(p2, p3, p4);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Genre)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.Genre) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.name, ((com.idlix.Genre) p6).name)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.slug, ((com.idlix.Genre) p6).slug)) {
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

    public final String getId()
    {
        return this.id;
    }

    public final String getName()
    {
        return this.name;
    }

    public final String getSlug()
    {
        return this.slug;
    }

    public int hashCode()
    {
        int v0_4;
        int v1_0 = 0;
        if (this.id != null) {
            v0_4 = this.id.hashCode();
        } else {
            v0_4 = 0;
        }
        String v3_1;
        int v2_1 = (v0_4 * 31);
        if (this.name != null) {
            v3_1 = this.name.hashCode();
        } else {
            v3_1 = 0;
        }
        int v0_1 = ((v2_1 + v3_1) * 31);
        if (this.slug != null) {
            v1_0 = this.slug.hashCode();
        }
        return (v0_1 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Genre(id=").append(this.id).append(", name=").append(this.name).append(", slug=").append(this.slug).append(41).toString();
    }
}
