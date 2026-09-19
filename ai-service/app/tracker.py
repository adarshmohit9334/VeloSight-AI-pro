import math
from typing import List, Dict, Any

class CentroidTracker:
    def __init__(self, max_disappeared: int = 15):
        self.next_object_id = 1
        self.objects: Dict[int, List[int]] = {}  # id -> centroid (cx, cy)
        self.tracked_data: Dict[int, Dict[str, Any]] = {} # id -> metadata (class, bbox, etc)
        self.disappeared: Dict[int, int] = {}
        self.max_disappeared = max_disappeared

    def register(self, centroid: List[int], data: Dict[str, Any]):
        self.objects[self.next_object_id] = centroid
        self.tracked_data[self.next_object_id] = data
        self.disappeared[self.next_object_id] = 0
        self.next_object_id += 1

    def deregister(self, object_id: int):
        del self.objects[object_id]
        if object_id in self.tracked_data:
            del self.tracked_data[object_id]
        del self.disappeared[object_id]

    def update(self, detections: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """
        Updates tracked objects with new detections.
        Returns list of detections augmented with 'track_id'.
        """
        if len(detections) == 0:
            for object_id in list(self.disappeared.keys()):
                self.disappeared[object_id] += 1
                if self.disappeared[object_id] > self.max_disappeared:
                    self.deregister(object_id)
            return []

        input_centroids = []
        for det in detections:
            x1, y1, x2, y2 = det['box']
            cx = int((x1 + x2) / 2.0)
            cy = int((y1 + y2) / 2.0)
            input_centroids.append((cx, cy))

        if len(self.objects) == 0:
            for i, centroid in enumerate(input_centroids):
                self.register(list(centroid), detections[i])
        else:
            object_ids = list(self.objects.keys())
            object_centroids = list(self.objects.values())

            # Calculate pairwise Euclidean distance matrix
            distances = []
            for oc in object_centroids:
                row = []
                for ic in input_centroids:
                    d = math.hypot(oc[0] - ic[0], oc[1] - ic[1])
                    row.append(d)
                distances.append(row)

            # Greedy assignment based on minimum distance
            used_rows = set()
            used_cols = set()

            for _ in range(min(len(object_centroids), len(input_centroids))):
                min_val = float('inf')
                min_r, min_c = -1, -1
                for r in range(len(object_centroids)):
                    if r in used_rows:
                        continue
                    for c in range(len(input_centroids)):
                        if c in used_cols:
                            continue
                        if distances[r][c] < min_val:
                            min_val = distances[r][c]
                            min_r, min_c = r, c

                if min_val > 150: # Distance threshold cap
                    break

                if min_r != -1 and min_c != -1:
                    object_id = object_ids[min_r]
                    self.objects[object_id] = list(input_centroids[min_c])
                    self.tracked_data[object_id] = detections[min_c]
                    self.disappeared[object_id] = 0
                    used_rows.add(min_r)
                    used_cols.add(min_c)

            # Handle unmatched existing objects
            unused_rows = set(range(len(object_centroids))) - used_rows
            for r in unused_rows:
                object_id = object_ids[r]
                self.disappeared[object_id] += 1
                if self.disappeared[object_id] > self.max_disappeared:
                    self.deregister(object_id)

            # Handle unmatched new detections
            unused_cols = set(range(len(input_centroids))) - used_cols
            for c in unused_cols:
                self.register(list(input_centroids[c]), detections[c])

        # Return tracked detections
        result = []
        for object_id, centroid in self.objects.items():
            det_info = self.tracked_data.get(object_id, {}).copy()
            det_info['track_id'] = object_id
            det_info['centroid'] = centroid
            result.append(det_info)

        return result
