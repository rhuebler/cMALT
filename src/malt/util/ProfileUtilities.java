/**
 * ProfileUtilities.java 
 * Copyright (C) 2015 Daniel H. Huson
 *
 * (Some files contain contributions from other authors, who are then mentioned separately.)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package malt.util;

import malt.AlignmentEngine;
import malt.analysis.OrganismsProfile;

/**
 * methods for merging profiles
 * Daniel Huson, 8.2014
 */
public class ProfileUtilities {
    /**
    /**
     * just get all organism profiles
     *
     * @param alignmentEngines
     * @return profiles
     */
    public static OrganismsProfile[] getOrganismsProfiles(AlignmentEngine[] alignmentEngines) {
        OrganismsProfile[] profiles = new OrganismsProfile[alignmentEngines.length];
        for (int i = 0; i < alignmentEngines.length; i++)
            profiles[i] = alignmentEngines[i].getOrganismsProfile();
        return profiles;
    }
}
