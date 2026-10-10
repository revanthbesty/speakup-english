insert into worlds(id, code, name, status)
values ('00000000-0000-0000-0000-000000000101','EVERYDAY_ENGLISH','Everyday English','PUBLISHED')
on conflict (code) do nothing;

insert into units(id, world_id, code, name)
select '00000000-0000-0000-0000-000000000102', id, 'FOOD_MEALS', 'Food & Meals'
from worlds where code='EVERYDAY_ENGLISH'
on conflict (code) do nothing;

insert into lessons(id, unit_id, code, name, objective, version)
select '00000000-0000-0000-0000-000000000103', id, 'BREAKFAST', 'Breakfast',
       'Use the verb eat to construct and speak a simple real-life sentence.', 1
from units where code='FOOD_MEALS'
on conflict (code) do nothing;

insert into activities(id, lesson_id, code, type, objective, status)
select '00000000-0000-0000-0000-000000000104', id, 'BREAKFAST_VERB_EAT', 'VERB_INTRODUCTION',
       'Understand and use the action verb eat.', 'PUBLISHED'
from lessons where code='BREAKFAST'
on conflict (code) do nothing;

insert into activities(id, lesson_id, code, type, objective, status)
select '00000000-0000-0000-0000-000000000105', id, 'BREAKFAST_SENTENCE_BUILDER', 'SENTENCE_BUILDER',
       'Build the sentence I eat rice for breakfast.', 'PUBLISHED'
from lessons where code='BREAKFAST'
on conflict (code) do nothing;

insert into activities(id, lesson_id, code, type, objective, status)
select '00000000-0000-0000-0000-000000000106', id, 'BREAKFAST_SPEAKING', 'SPEAKING',
       'Answer What do you eat for breakfast? in English.', 'PUBLISHED'
from lessons where code='BREAKFAST'
on conflict (code) do nothing;

insert into activity_versions(activity_id, version, payload, published_at)
select id, 1,
jsonb_build_object(
  'verb','eat',
  'meaning','to put food in your mouth and swallow it',
  'example','I eat rice for breakfast.',
  'prompt','What do you eat for breakfast?',
  'validPatterns', jsonb_build_array('I eat rice for breakfast.','I eat breakfast at home.')
), now()
from activities
where code='BREAKFAST_VERB_EAT'
on conflict(activity_id, version) do nothing;

insert into activity_versions(activity_id, version, payload, published_at)
select id, 1,
jsonb_build_object(
  'target','I eat rice for breakfast.',
  'tokens',jsonb_build_array('I','eat','rice','for','breakfast'),
  'skill','SENTENCE_CONSTRUCTION'
), now()
from activities
where code='BREAKFAST_SENTENCE_BUILDER'
on conflict(activity_id, version) do nothing;

insert into activity_versions(activity_id, version, payload, published_at)
select id, 1,
jsonb_build_object(
  'prompt','What do you eat for breakfast?',
  'expectedIntent','Describe breakfast food using the verb eat.',
  'minimumDurationSeconds',3,
  'maximumDurationSeconds',30
), now()
from activities
where code='BREAKFAST_SPEAKING'
on conflict(activity_id, version) do nothing;
