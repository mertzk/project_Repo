List = [5,1]
Goal = 15
def yolo(List, Goal):
    Sum = 0
    if Goal == 0:
        return 1
    if len(List) > 0 and Goal+List[0] >0:
        Sum = Sum + yolo(List,Goal-List[0])
    if len(List) > 0:
        Sum = Sum + yolo(List[1:], Goal)
    return Sum
print(yolo(List,Goal))
