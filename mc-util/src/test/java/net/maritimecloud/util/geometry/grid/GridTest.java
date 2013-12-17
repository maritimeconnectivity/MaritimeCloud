/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.maritimecloud.util.geometry.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import net.maritimecloud.util.geometry.BoundingBox;
import net.maritimecloud.util.geometry.Position;

import org.junit.BeforeClass;
import org.junit.Test;

public class GridTest {

    private static final int GRID_SIZE = 200;

    static Grid grid;

    @BeforeClass
    public static void setup() {
        grid = Grid.createSize(GRID_SIZE);
    }

    @Test
    public void testGetResolution() {
        assertEquals(0.0017966313162819712, grid.getResolution(), 1e-18);
    }

    @Test
    public void testGetSize() {
        // Test #1
        assertEquals(GRID_SIZE, grid.getSize(), 1e-18);

        // Test #2
        Grid grid2 = Grid.createSize(50);
        assertEquals(50, grid2.getSize(), 1e-18);

        // Test #3
        Grid grid3 = Grid.createSize(250);
        assertEquals(250, grid3.getSize(), 1e-18);
    }

    @Test
    public void testGetCellFromCellId() {
        Cell cell1 = grid.getCell(1L);
        assertEquals(1L, cell1.getCellId());
        Cell cell2 = grid.getCell(6034900978L);
        assertEquals(6034900978L, cell2.getCellId());
    }

    @Test
    public void testGetGeoPosOfCell() {
        Cell cell = grid.getCell(Position.create(56.0, 12.0));
        Position southWestCorner = grid.getGeoPosOfCell(cell);
        assertEquals(55.999201497192765d, southWestCorner.getLatitude(), 1e-10);
        assertEquals(11.999700561447286d, southWestCorner.getLongitude(), 1e-10);
    }

    @Test
    public void testGetBoundingBoxOfCell() {
        Cell cell1 = grid.getCell(Position.create(56.0, 12.0));
        assertEquals(6245495054L, cell1.getCellId());

        BoundingBox boundingBox1 = grid.getBoundingBoxOfCell(cell1);

        assertEquals(56.000998128509046d, boundingBox1.getMaxLat(), 1e-10);
        assertEquals(55.999201497192765d, boundingBox1.getMinLat(), 1e-10);
        assertEquals(12.001497192763567d, boundingBox1.getMaxLon(), 1e-10);
        assertEquals(11.999700561447286d, boundingBox1.getMinLon(), 1e-10);
    }

    @Test
    public void testGetCellEastOf() {
        final double commonLatitudeNorth = 56.000998128509046d;
        final double commonLatitudeSouth = 55.999201497192765d;

        Cell startCell = grid.getCell(Position.create(56.0, 12.0));
        BoundingBox boundingBox1 = grid.getBoundingBoxOfCell(startCell);

        assertEquals(commonLatitudeNorth, boundingBox1.getMaxLat(), 1e-10);
        assertEquals(commonLatitudeSouth, boundingBox1.getMinLat(), 1e-10);
        assertEquals(12.001497192763567d, boundingBox1.getMaxLon(), 1e-10);
        assertEquals(11.999700561447286d, boundingBox1.getMinLon(), 1e-10);

        Cell eastCell = grid.getCellEastOf(startCell);
        BoundingBox boundingBox2 = grid.getBoundingBoxOfCell(eastCell);

        assertEquals(startCell.getCellId() + 1, eastCell.getCellId());
        assertEquals(commonLatitudeNorth, boundingBox2.getMaxLat(), 1e-10);
        assertEquals(commonLatitudeSouth, boundingBox2.getMinLat(), 1e-10);
        assertEquals(12.003293824079849d, boundingBox2.getMaxLon(), 1e-10);
        assertEquals(12.001497192763567d, boundingBox2.getMinLon(), 1e-10);
    }

    @Test
    public void testGetCellEastWestOfCellWithWestBorderAtGreenwichLine() {
        // This cell is a border cell along the east side of the Greenwich line (longitude 0) where the
        // cell-id changes with +1 to the east, and +multiplier-1 to the west (counting all way round the globe).
        final long greenwichCellId = 6011250000L; // cellId % multiplier == 0

        Cell greenwichCell = grid.getCell(greenwichCellId);
        System.out.println("greenwichCell: " + grid.getBoundingBoxOfCell(greenwichCell).toString());
        assertEquals(0.0000000000000000000, grid.getBoundingBoxOfCell(greenwichCell).getMinLon(), 1e-18);
        assertEquals(0.0017966313162819712, grid.getBoundingBoxOfCell(greenwichCell).getMaxLon(), 1e-18);

        // Test that cell east of greenwich cell has id +1
        Cell eastOfGreenwich = grid.getCellEastOf(greenwichCell);
        System.out.println("eastOfGreenwich: " + grid.getBoundingBoxOfCell(eastOfGreenwich));
        assertEquals(greenwichCellId + 1L, eastOfGreenwich.getCellId());

        // Test that cell west of greenwich has id +multiplier-1
        Cell westOfGreenwich = grid.getCellWestOf(greenwichCell);
        System.out.println("westOfGreenwich: " + grid.getBoundingBoxOfCell(westOfGreenwich).toString());
        assertEquals(greenwichCellId + (long) grid.multiplier - 1, westOfGreenwich.getCellId());
    }

    @Test
    public void testGetCellEastWestOfCellWithEastBorderAtGreenwichLine() {
        // This cell is a border cell along the west side of the Greenwich line (longitude 0) where the
        // cell-id changes with +multiplier+1 to the east, and -1 to the west.
        final long greenwichCellId = 6011250000L - 1;
        Cell greenwichCell = grid.getCell(greenwichCellId);
        System.out.println("greenwichCell: " + grid.getBoundingBoxOfCell(greenwichCell).toString());
        assertEquals(-0.0017966313162819712, grid.getBoundingBoxOfCell(greenwichCell).getMinLon(), 1e-18);
        assertEquals(0.0000000000000000000, grid.getBoundingBoxOfCell(greenwichCell).getMaxLon(), 1e-18);

        // Test that cell east of greenwich cell has id +multiplier+1
        Cell eastOfGreenwichCell = grid.getCellEastOf(greenwichCell);
        System.out.println("eastOfGreenwich: " + grid.getBoundingBoxOfCell(eastOfGreenwichCell).toString());
        assertEquals(greenwichCellId - (long) grid.multiplier + 1, eastOfGreenwichCell.getCellId());

        // Test that cell west of greenwich has id -1
        Cell westOfGreenwichCell = grid.getCellWestOf(greenwichCell);
        System.out.println("westOfGreenwich: " + grid.getBoundingBoxOfCell(westOfGreenwichCell).toString());
        assertEquals(greenwichCellId - 1, westOfGreenwichCell.getCellId());
    }

    @Test
    public void testGetCellWestOf() {
        final double commonLatitudeNorth = 56.000998128509046d;
        final double commonLatitudeSouth = 55.999201497192765d;

        Cell startCell = grid.getCell(Position.create(56.0, 12.0));
        BoundingBox boundingBox1 = grid.getBoundingBoxOfCell(startCell);

        assertEquals(commonLatitudeNorth, boundingBox1.getMaxLat(), 1e-10);
        assertEquals(commonLatitudeSouth, boundingBox1.getMinLat(), 1e-10);
        assertEquals(12.001497192763567d, boundingBox1.getMaxLon(), 1e-10);
        assertEquals(11.999700561447286d, boundingBox1.getMinLon(), 1e-10);

        Cell westCell = grid.getCellWestOf(startCell);
        BoundingBox boundingBox2 = grid.getBoundingBoxOfCell(westCell);

        assertEquals(startCell.getCellId() - 1, westCell.getCellId());
        assertEquals(commonLatitudeNorth, boundingBox2.getMaxLat(), 1e-10);
        assertEquals(commonLatitudeSouth, boundingBox2.getMinLat(), 1e-10);
        assertEquals(11.999700561447286d, boundingBox2.getMaxLon(), 1e-10);
        assertEquals(11.997903930131004d, boundingBox2.getMinLon(), 1e-10);
    }

    @Test
    public void testGetCellNorthOf() {
        Cell southCell = grid.getCell(Position.create(56.0, 12.0));
        Cell northCell = grid.getCell(Position.create(56.0 + grid.getResolution() * 1.1, 12.0));
        Cell furtherNorthCell = grid.getCell(Position.create(56.0 + grid.getResolution() * 2.1, 12.0));
        System.out.println("southCell: " + southCell.getCellId() + ", northCell: " + northCell.getCellId()
                + ", furtherNorthCell: " + furtherNorthCell.getCellId());

        assertEquals(northCell.getCellId(), grid.getCellNorthOf(southCell).getCellId());
        assertEquals(furtherNorthCell.getCellId(), grid.getCellNorthOf(northCell).getCellId());
    }

    @Test
    public void testGetCellSouthOf() {
        Cell northCell = grid.getCell(Position.create(56.0, 12.0));
        Cell southCell = grid.getCell(Position.create(56.0 - grid.getResolution() * 1.1, 12.0));
        Cell furtherSouthCell = grid.getCell(Position.create(56.0 - grid.getResolution() * 2.1, 12.0));
        System.out.println("northCell: " + northCell.getCellId() + ", southCell: " + southCell.getCellId()
                + ", furtherSouthCell: " + furtherSouthCell.getCellId());

        assertEquals(southCell.getCellId(), grid.getCellSouthOf(northCell).getCellId());
        assertEquals(furtherSouthCell.getCellId(), grid.getCellSouthOf(southCell).getCellId());
    }

    @Test
    public void testGetCellsInBoundingBox() {
        Cell cell1 = grid.getCell(Position.create(56.0, 12.0));
        assertEquals(6245495054L, cell1.getCellId());

        // Test for small area
        BoundingBox smallArea = grid.getBoundingBoxOfCell(cell1);
        Set<Cell> cells = grid.getCells(smallArea);
        assertNotNull(cells);
        assertEquals(1, cells.size());
        Cell cell = cells.iterator().next();
        assertEquals(6245495054L, cell.getCellId());

        // Test for larger area (but still somewhat small....)
        Cell cell2 = grid.getCell(Position.create(55.99, 11.99));
        BoundingBox largerArea = grid.getBoundingBoxOfCell(cell1).include(grid.getBoundingBoxOfCell(cell2));

        cells = grid.getCells(largerArea);

        assertNotNull(cells);
        assertEquals(49, cells.size());
        Iterator<Cell> iterator = cells.iterator();
        while (iterator.hasNext()) {
            cell = iterator.next();
            Position geoPosOfCell = grid.getGeoPosOfCell(cell);
            assertTrue(largerArea.contains(geoPosOfCell));
            assertTrue(geoPosOfCell.getLatitude() <= largerArea.getMaxLat());
            assertTrue(geoPosOfCell.getLatitude() >= largerArea.getMinLat());
            assertTrue(geoPosOfCell.getLongitude() <= largerArea.getMaxLon());
            assertTrue(geoPosOfCell.getLongitude() >= largerArea.getMinLon());
            System.out.println("Cell id " + cell.getCellId() + " " + geoPosOfCell + " is inside bounding box "
                    + largerArea);
        }
    }
}
